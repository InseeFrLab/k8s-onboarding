package io.insee.dev.k8sonboarding.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceQuota;
import io.fabric8.kubernetes.api.model.ResourceQuotaBuilder;
import io.fabric8.kubernetes.api.model.ResourceQuotaFluent;
import io.fabric8.kubernetes.api.model.rbac.RoleBinding;
import io.fabric8.kubernetes.api.model.rbac.RoleBindingBuilder;
import io.fabric8.kubernetes.api.model.rbac.SubjectBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.insee.dev.k8sonboarding.configuration.KubernetesClientProvider;
import io.insee.dev.k8sonboarding.configuration.properties.ClusterProperties;
import io.insee.dev.k8sonboarding.configuration.properties.QuotaProperties;
import io.insee.dev.k8sonboarding.model.User;
import io.insee.dev.k8sonboarding.view.ClusterCredentials;

@Service
public class OnboardingService {

    private static final Logger logger = LoggerFactory.getLogger(OnboardingService.class);

	public static final String ADMIN = "admin";
	public static final String API_GROUP = "rbac.authorization.k8s.io";
	public static final String USER = "User";
	public static final String GROUP = "Group";
	public static final String LABEL_CREATED_BY = "created_by";
	public static final String CLUSTER_ROLE = "ClusterRole";

	public static final String NO_QUOTA_VALUE="0";
	public static final String RESOURCE_QUOTA_REQUESTS_STORAGE = "requests.storage";
	
	@Value("${spring.application.name:k8s-onboarding}")
	private String appName;
	
	@Autowired
    QuotaProperties quotaProperties;
	
	@Autowired
	ClusterProperties clusterProperty;

	@Autowired
	KubernetesClientProvider kubernetesClientProvider;

	@Autowired
	private KubernetesClient kubernetesClient;

	@Value("${io.insee.dev.k8sonboarding.does-remove-suffix}")
	private boolean doesRemoveSuffix;

	public OnboardingService(ClusterProperties clusterProperty, KubernetesClientProvider kubernetesClientProvider) {
		super();
		this.clusterProperty = clusterProperty;
		this.kubernetesClientProvider = kubernetesClientProvider;
	}

	/**
	 * Currently, namespaceid is ignored
	 *
	 * @param user
	 * @param groupId
	 */
	public void createNamespace(User user, String groupId) {
		String namespaceId = getNamespaceId(user, groupId);
		if (!checkNamespaceExists(namespaceId)) {
			logger.info("creating namespace {}", namespaceId);
			Namespace ns = new NamespaceBuilder().withNewMetadata().withName(namespaceId)
					.addToLabels(LABEL_CREATED_BY, appName).endMetadata().build();
			kubernetesClient.namespaces().resource(ns).create();			
	    
	    applyQuotas(namespaceId, quotaProperties, true);
		}
	}
	    
    /**
     * 
     * @param namespaceId
     * @param inputQuota
     * @param overrideExisting
     */
    private void applyQuotas(String namespaceId, QuotaProperties inputQuota, boolean overrideExisting) {
        ResourceQuotaBuilder resourceQuotaBuilder = new ResourceQuotaBuilder();
        resourceQuotaBuilder.withNewMetadata()
                .withLabels(Map.of(LABEL_CREATED_BY, appName))
                .withName(namespaceId)
                .withNamespace(namespaceId)
                .endMetadata();

        Map<String, String> quotasToApply = inputQuota.asMap();

        if (quotasToApply.entrySet().stream().filter(e -> e.getValue() != null).count() == 0) {
            return;
        }

        ResourceQuotaFluent.SpecNested<ResourceQuotaBuilder> resourceQuotaBuilderSpecNested = resourceQuotaBuilder
                .withNewSpec();
        quotasToApply.entrySet().stream().filter(e -> e.getValue() != null).forEach(e -> resourceQuotaBuilderSpecNested.addToHard(e.getKey(),Quantity.parse(e.getValue())));
        resourceQuotaBuilderSpecNested.endSpec();

        ResourceQuota quota = resourceQuotaBuilder.build();
        if (overrideExisting) {
            kubernetesClient.resource(quota).inNamespace(namespaceId).createOrReplace();
        }
        else {
            try {
                kubernetesClient.resource(quota).inNamespace(namespaceId).create();
            }
            catch (KubernetesClientException e) {
                if (e.getCode() != 409) {
                    // This is not a "quota already in place" error
                    throw e;
                }
            }
        }
    }
    
    
	/**
	 * Currently, namespaceid is ignored
	 *
	 * @param user
	 * @param group
	 */
	public void addPermissionsToNamespace(User user, String group) {
		String namespaceId = getNamespaceId(user, group);
		final String userId = getUserIdPrefixed(user.getId());
		final String groupId = getGroupIdPrefixed(group);
		if (checkNamespaceExists(namespaceId) && !checkPermissionsExists(namespaceId)) {
			logger.info("creating rolebinding for user {}", userId);
			RoleBindingBuilder bindingToCreate = new RoleBindingBuilder().withNewMetadata()
					.withLabels(Map.of(LABEL_CREATED_BY, appName))
					.withName(clusterProperty.getNameNamespaceAdmin()).withNamespace(namespaceId).endMetadata()
					.withNewRoleRef().withApiGroup(API_GROUP).withKind(CLUSTER_ROLE).withName(ADMIN).endRoleRef();
			if (group == null) {
				bindingToCreate = bindingToCreate.withSubjects(new SubjectBuilder().withKind(USER).withName(userId)
						.withApiGroup(API_GROUP).withNamespace(namespaceId).build());
			} else {
				bindingToCreate = bindingToCreate.withSubjects(new SubjectBuilder().withKind(GROUP).withName(groupId)
						.withApiGroup(API_GROUP).withNamespace(namespaceId).build());
			}		
			kubernetesClient.resource(bindingToCreate.build()).inNamespace(namespaceId).create();
		}
	}

	

      public boolean checkNamespaceExists(String namespaceId) {
		final Namespace namespace = kubernetesClient.namespaces().withName(namespaceId).get();
		return namespace != null;
	}

	public boolean checkPermissionsExists(String namespaceId) {	    
	    final RoleBinding roleBinding = kubernetesClient.rbac().roleBindings().inNamespace(namespaceId)
				.withName(clusterProperty.getNameNamespaceAdmin()).get();
		return (roleBinding != null && !roleBinding.getSubjects().isEmpty());
	}

	public ClusterCredentials getClusterCredentials(User user, String group) {
		final ClusterCredentials clusterCredentials = new ClusterCredentials();
		final String namespaceId = getNamespaceId(user, group);
		clusterCredentials.setApiserverUrl(clusterProperty.getApiserverUrl());
		clusterCredentials.setNamespace(namespaceId);
		clusterCredentials.setToken(user.getAuthToken());
		clusterCredentials.setUser(getUserIdPrefixed(user.getId()));
		clusterCredentials.setOnboarded(checkPermissionsExists(namespaceId));
		clusterCredentials.setClusterName(clusterProperty.getClusterName());
		clusterCredentials.setInsecure(clusterProperty.getInsecure());
		return clusterCredentials;
	}

	private String getNamespaceId(User user, String group) {
		if (group == null) {
			return clusterProperty.getNamespacePrefix() + clusterProperty.getUserPrefix() + user.getId();
		}
		String cleanedAndSanitizedGroup = sanitize(optionallyRemoveSuffix(group));
		return clusterProperty.getNamespaceGroupPrefix() + clusterProperty.getGroupPrefix() + cleanedAndSanitizedGroup;
	}

	private String optionallyRemoveSuffix(String rawGroup) {
		if (doesRemoveSuffix){
			return StringUtils.substringBefore(rawGroup, '_');
		}
		return rawGroup;
	}

	private String sanitize(String cleanedGroup) {
		if (cleanedGroup != null) {
			var sanitizedGroup = cleanedGroup.toLowerCase();
			var anythingButAlphanumericAndDash = "[^-a-z0-9]";
			sanitizedGroup = sanitizedGroup.replaceAll(anythingButAlphanumericAndDash, "-");
			return sanitizedGroup;
		}
		return cleanedGroup;
	}

	private String getUserIdPrefixed(String id) {
		return clusterProperty.getUserPrefix() + id;
	}

	private String getGroupIdPrefixed(String id) {
		return clusterProperty.getGroupPrefix() + id;
	}

	public KubernetesClient getKubernetesClient() {
		return kubernetesClient;
	}

	public void setKubernetesClient(KubernetesClient kubernetesClient) {
		this.kubernetesClient = kubernetesClient;
	}
}

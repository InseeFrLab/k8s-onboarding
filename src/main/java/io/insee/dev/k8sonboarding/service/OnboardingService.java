package io.insee.dev.k8sonboarding.service;

import java.util.Map;

import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.fabric8.kubernetes.api.model.DoneableNamespace;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.rbac.DoneableRoleBinding;
import io.fabric8.kubernetes.api.model.rbac.RoleBinding;
import io.fabric8.kubernetes.api.model.rbac.SubjectBuilder;
import io.insee.dev.k8sonboarding.configuration.KubernetesClientProvider;
import io.insee.dev.k8sonboarding.configuration.properties.ClusterProperties;
import io.insee.dev.k8sonboarding.controller.ClusterAccessController;
import io.insee.dev.k8sonboarding.model.User;
import io.insee.dev.k8sonboarding.view.ClusterCredentials;

@Service
public class OnboardingService {

	private static final Logger logger = LoggerFactory.getLogger(ClusterAccessController.class);

	public static final String ADMIN = "admin";
	public static final String API_GROUP = "rbac.authorization.k8s.io";
	public static final String USER = "User";
	public static final String GROUP = "Group";
	public static final String LABEL_CREATED_BY = "created_by";
	public static final String CLUSTER_ROLE = "ClusterRole";

	@Value("${spring.application.name:k8s-onboarding}")
	private String appName;

	@Autowired
	ClusterProperties clusterProperty;

	@Autowired
	KubernetesClientProvider kubernetesClientProvider;

	@Autowired
	private KubernetesClient kubernetesClient;

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
			final DoneableNamespace namespaceToCreate = kubernetesClient.namespaces().createNew().withNewMetadata()
					.withName(namespaceId).addToLabels(LABEL_CREATED_BY, appName).endMetadata();
			namespaceToCreate.done();
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
			DoneableRoleBinding bindingToCreate = kubernetesClient.rbac().roleBindings().inNamespace(namespaceId)
					.createNew().withNewMetadata().withLabels(Map.of(LABEL_CREATED_BY, appName))
					.withName(clusterProperty.getNameNamespaceAdmin()).withNamespace(namespaceId).endMetadata()
					.withNewRoleRef().withApiGroup(API_GROUP).withKind(CLUSTER_ROLE).withName(ADMIN).endRoleRef();
			if (group == null) {
				bindingToCreate = bindingToCreate.withSubjects(new SubjectBuilder().withKind(USER).withName(userId)
						.withApiGroup(API_GROUP).withNamespace(namespaceId).build());
			} else {
				bindingToCreate = bindingToCreate.withSubjects(new SubjectBuilder().withKind(GROUP).withName(groupId)
						.withApiGroup(API_GROUP).withNamespace(namespaceId).build());
			}
			bindingToCreate.done();
		}
	}

	public Boolean checkNamespaceExists(String namespaceId) {
		final Namespace namespace = kubernetesClient.namespaces().withName(namespaceId).get();
		return namespace != null;
	}

	public Boolean checkPermissionsExists(String namespaceId) {
		final RoleBinding roleBinding = kubernetesClient.rbac().roleBindings().inNamespace(namespaceId)
				.withName(clusterProperty.getNameNamespaceAdmin()).get();
		if (roleBinding != null && roleBinding.getSubjects().size() > 0) {
			return true;
		}
		return false;
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
		return clusterProperty.getNamespaceGroupPrefix() + clusterProperty.getGroupPrefix() + group;
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

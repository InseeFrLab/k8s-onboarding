package io.insee.dev.k8sonboarding.service;

import java.util.Map;

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

    public static final String CLUSTER_ADMIN = "cluster-admin";
    public static final String API_GROUP = "rbac.authorization.k8s.io";
    public static final String USER = "User";
    public static final String LABEL_CREATED_BY = "created_by";
    public static final String CLUSTER_ROLE = "ClusterRole";

    @Value("${spring.application.name:k8s-onboarding}")
    private String appName;

    @Autowired
    ClusterProperties clusterProperty;

    @Autowired
    KubernetesClientProvider kubernetesClientProvider;

    public OnboardingService(ClusterProperties clusterProperty, KubernetesClientProvider kubernetesClientProvider) {
	super();
	this.clusterProperty = clusterProperty;
	this.kubernetesClientProvider = kubernetesClientProvider;
    }

    /**
     * Currently, namespaceid is ignored
     *
     * @param user
     * @param namespaceId
     */
    public void createNamespace(User user, String namespaceId) {
	namespaceId = getNameSpaceId(user.getId());
	if (!checkNamespaceExists(namespaceId)) {
	    logger.info("creating namespace {}", namespaceId);
	    final DoneableNamespace namespaceToCreate = kubernetesClientProvider.getKubernetesClient().namespaces()
		    .createNew().withNewMetadata().withName(namespaceId).addToLabels(LABEL_CREATED_BY, appName)
		    .endMetadata();
	    namespaceToCreate.done();
	}
    }

    /**
     * Currently, namespaceid is ignored
     *
     * @param user
     * @param namespaceId
     */
    public void addPermissionsToNamespace(User user, String namespaceId) {
	namespaceId = getNameSpaceId(user.getId());
	final String userId = getUserIdPrefixed(user.getId());
	if (checkNamespaceExists(namespaceId) && !checkPermissionsExists(namespaceId, user.getId())) {
	    logger.info("creating rolebinding for user {}", userId);
	    final DoneableRoleBinding bindingToCreate = kubernetesClientProvider.getKubernetesClient().rbac()
		    .roleBindings().inNamespace(namespaceId).createNew().withNewMetadata()
		    .withLabels(Map.of(LABEL_CREATED_BY, appName)).withName(clusterProperty.getNameNamespaceAdmin())
		    .withNamespace(namespaceId).endMetadata()
		    .withSubjects(new SubjectBuilder().withKind(USER).withName(userId).withApiGroup(API_GROUP)
			    .withNamespace(namespaceId).build())
		    .withNewRoleRef().withApiGroup(API_GROUP).withKind(CLUSTER_ROLE).withName(CLUSTER_ADMIN)
		    .endRoleRef();
	    bindingToCreate.done();
	}
    }

    public Boolean checkNamespaceExists(String namespaceId) {
	final Namespace namespace = kubernetesClientProvider.getKubernetesClient().namespaces().withName(namespaceId)
		.get();
	return namespace != null;
    }

    public Boolean checkPermissionsExists(String namespaceId, String userId) {
	final RoleBinding roleBinding = kubernetesClientProvider.getKubernetesClient().rbac().roleBindings()
		.inNamespace(namespaceId).withName(clusterProperty.getNameNamespaceAdmin()).get();
	if (roleBinding != null && roleBinding.getSubjects().size() > 0) {
	    return userId.equalsIgnoreCase(roleBinding.getSubjects().get(0).getName());
	}
	return false;
    }

    public ClusterCredentials getClusterCredentials(User user) {
	final ClusterCredentials clusterCredentials = new ClusterCredentials();
	final String namespaceId = getNameSpaceId(user.getId());
	clusterCredentials.setApiserverUrl(clusterProperty.getApiserverUrl());
	clusterCredentials.setNamespace(namespaceId);
	clusterCredentials.setToken(user.getAuthToken());
	clusterCredentials.setUser(getUserIdPrefixed(user.getId()));
	clusterCredentials.setOnboarded(checkPermissionsExists(namespaceId,getUserIdPrefixed(user.getId())));
	clusterCredentials.setClusterName(clusterProperty.getClusterName());
	return clusterCredentials;
    }

    private String getNameSpaceId(String id) {
	return clusterProperty.getNamespacePrefix() + clusterProperty.getUserPrefix() + id;
    }

    private String getUserIdPrefixed(String id) {
	return clusterProperty.getUserPrefix() + id;
    }
}

package io.insee.dev.k8sonboarding.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.fabric8.kubernetes.api.model.DoneableNamespace;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.rbac.DoneableRoleBinding;
import io.fabric8.kubernetes.api.model.rbac.SubjectBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.insee.dev.k8sonboarding.model.User;
import io.insee.dev.k8sonboarding.property.ClusterProperties;
import io.insee.dev.k8sonboarding.view.ClusterCredentials;

@Service
public class OnboardingService {

    private static final String CLUSTER_ADMIN = "cluster-admin";
    private static final String API_GROUP = "rbac.authorization.k8s.io";
    private static final String USER = "User";
    private static final String LABEL_CREATED_BY = "created_by";
    public static final String CLUSTER_ROLE = "ClusterRole";

    @Value("${spring.application.name:k8s-onboarding}")
    private String appName;

    @Autowired
    private ClusterProperties clusterProperty;

    public void onboard(User user) {
	try (final KubernetesClient client = new DefaultKubernetesClient()) {
	    final String userId = user.getId();
	    final String namespaceId = getNameSpaceId(user.getId());
	    final DoneableNamespace namespaceToCreate = client.namespaces().createNew().withNewMetadata()
		    .withName(namespaceId).addToLabels(LABEL_CREATED_BY, appName).endMetadata();

	    final DoneableRoleBinding bindingToCreate = client.rbac().roleBindings().inNamespace(namespaceId)
		    .createNew().withNewMetadata().withLabels(Map.of(LABEL_CREATED_BY, appName))
		    .withName(clusterProperty.getNameNamespaceAdmin()).withNamespace(namespaceId).endMetadata()
		    .withSubjects(new SubjectBuilder().withKind(USER).withName(userId).withApiGroup(API_GROUP)
			    .withNamespace(namespaceId).build())
		    .withNewRoleRef().withApiGroup(API_GROUP).withKind(CLUSTER_ROLE).withName(CLUSTER_ADMIN)
		    .endRoleRef();

	    namespaceToCreate.done();
	    bindingToCreate.done();
	}
    }

    public Boolean checkNamespaceExists(User user) {
	try (final KubernetesClient client = new DefaultKubernetesClient()) {
	    final String namespaceId = getNameSpaceId(user.getId());
	    final Namespace namespace = client.namespaces().withName(namespaceId).get();
	    return namespace == null ? Boolean.FALSE : Boolean.TRUE;
	}
    }

    public ClusterCredentials getClusterCredentials(User user) {
	final ClusterCredentials clusterCredentials = new ClusterCredentials();
	clusterCredentials.setApiserverUrl(clusterProperty.getApiserverUrl());
	clusterCredentials.setNamespace(getNameSpaceId(user.getId()));
	clusterCredentials.setToken(user.getAuthToken());
	clusterCredentials.setUser(getUserPrefixId(user.getId()));
	return clusterCredentials;
    }

    private String getNameSpaceId(String id) {
	return clusterProperty.getNamespacePrefix() + clusterProperty.getUserPrefix() + id;
    }

    private String getUserPrefixId(String id) {
	return clusterProperty.getUserPrefix() + id;
    }
}

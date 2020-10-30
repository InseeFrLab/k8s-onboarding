package io.insee.dev.k8sonboarding.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import io.fabric8.kubernetes.api.model.DoneableNamespace;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.rbac.DoneableRoleBinding;
import io.fabric8.kubernetes.api.model.rbac.SubjectBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.insee.dev.k8sonboarding.model.User;

@Service
public class OnboardingService {

    public void onboard(User user) {
      try(final KubernetesClient client = new DefaultKubernetesClient()){
    	final String userId = user.getId();
    	final String namespaceId = "prefix" + "-" + user.getId();
    	final DoneableNamespace namespaceToCreate = client.namespaces().createNew().withNewMetadata()
    		.withName(namespaceId).addToLabels("created_by", "k8s-onboarding").endMetadata();
    
    	final DoneableRoleBinding bindingToCreate = client.rbac().roleBindings().inNamespace(namespaceId).createNew()
    		.withNewMetadata().withLabels(Map.of("created_by", "k8s-onboarding")).withName("namespace_admin")
    		.withNamespace(namespaceId).endMetadata()
    		.withSubjects(new SubjectBuilder().withKind("User").withName(userId)
    			.withApiGroup("rbac.authorization.k8s.io").withNamespace(namespaceId).build())
    		.withNewRoleRef().withApiGroup("rbac.authorization.k8s.io").withKind("ClusterRole")
    		.withName("cluster-admin").endRoleRef();
    
    	namespaceToCreate.done();
    	bindingToCreate.done();
      }
    }

    public Boolean checkNamespaceExists(User user) {
      try(final KubernetesClient client = new DefaultKubernetesClient()){
        final String namespaceId = "prefix" + "-" + user.getId();
        final Namespace namespace = client.namespaces().withName(namespaceId).get();
        return namespace == null ? Boolean.FALSE : Boolean.TRUE;
      }
    }
}

package io.insee.dev.k8sonboarding.service;

import io.fabric8.kubernetes.api.model.DoneableNamespace;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.rbac.DoneableRoleBinding;
import io.fabric8.kubernetes.api.model.rbac.SubjectBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.insee.dev.k8sonboarding.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OnboardingService {

    public void onboard(User user) {
        KubernetesClient client = new DefaultKubernetesClient();
        String userId = user.getId();
        String namespaceId = "prefix"+"-"+user.getId();
        DoneableNamespace namespaceToCreate = client.namespaces().createNew().withNewMetadata().withName(namespaceId)
                .addToLabels("created_by","k8s-onboarding").endMetadata();

        DoneableRoleBinding bindingToCreate = client.rbac().roleBindings().inNamespace(namespaceId).createNew()
                .withNewMetadata()
                .withLabels(Map.of("created_by","k8s-onboarding"))
                .withName("namespace_admin").withNamespace(namespaceId).endMetadata()
                .withSubjects(new SubjectBuilder().withKind("User").withName(userId)
                        .withApiGroup("rbac.authorization.k8s.io").withNamespace(namespaceId).build())
                .withNewRoleRef().withApiGroup("rbac.authorization.k8s.io").withKind("ClusterRole").withName("cluster-admin").endRoleRef();

        Namespace namespace = namespaceToCreate.done();
        bindingToCreate.done();
    }
}

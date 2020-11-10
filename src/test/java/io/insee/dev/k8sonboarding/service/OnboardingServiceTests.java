package io.insee.dev.k8sonboarding.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.rbac.RoleBindingList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.EnableKubernetesMockClient;
import io.insee.dev.k8sonboarding.configuration.properties.ClusterProperties;
import io.insee.dev.k8sonboarding.model.User;
import io.insee.dev.k8sonboarding.view.ClusterCredentials;

@EnableKubernetesMockClient(crud = true)
public class OnboardingServiceTests {

    KubernetesClient client;

    private final ClusterProperties clusterProperties;

    private final User user;

    public OnboardingServiceTests() {
	super();
	final ClusterProperties clusterProperties = Mockito.mock(ClusterProperties.class);
	Mockito.when(clusterProperties.getNamespacePrefix()).thenReturn("namespaceprefix-");
	Mockito.when(clusterProperties.getUserPrefix()).thenReturn("userprefix-");
	Mockito.when(clusterProperties.getApiserverUrl()).thenReturn("apiserver-url");
	this.clusterProperties = clusterProperties;
	final User user = new User();
	user.setId("id");
	user.setAuthToken("authToken");
	this.user = user;
    }

	/**

	@Test
    public void checkNamespaceExistsTest() {
	// mock.expect().withPath("/api/v1/namespaces/namespaceprefix-userprefix-id")
	// .andReturn(200, new NamespaceBuilder().build()).once();
	client.namespaces().createNew().withNewMetadata().withName("namespaceprefix-userprefix-id").endMetadata()
		.done();
	final OnboardingService onBoardingService = new OnboardingService(this.clusterProperties, this.client);
	Assert.assertEquals(Boolean.TRUE, onBoardingService.checkNamespaceExists("namespaceprefix-userprefix-id"));
    }

    @Test
    public void checkNamespaceDontExists() {
	final OnboardingService onBoardingService = new OnboardingService(this.clusterProperties, this.client);
	Assert.assertEquals(Boolean.FALSE, onBoardingService.checkNamespaceExists("namespaceprefix-userprefix-id"));
    }

    @Test
    public void onboardTests() {
	final OnboardingService onBoardingService = new OnboardingService(this.clusterProperties, this.client);
	ReflectionTestUtils.setField(onBoardingService, "appName", "appName");
	//onBoardingService.onboard(user);
	final Namespace namespace = client.namespaces().withName("namespaceprefix-userprefix-id").get();
	Assert.assertEquals(namespace.getMetadata().getLabels().size(), 1);
	Assert.assertEquals(namespace.getMetadata().getLabels().get(OnboardingService.LABEL_CREATED_BY), "appName");
	final RoleBindingList roleBindingList = client.rbac().roleBindings()
		.inNamespace("namespaceprefix-userprefix-id").list();
	Assert.assertEquals(roleBindingList.getItems().size(), 1);
	Assert.assertEquals(roleBindingList.getItems().get(0).getMetadata().getLabels().size(), 1);
	Assert.assertEquals(
		roleBindingList.getItems().get(0).getMetadata().getLabels().get(OnboardingService.LABEL_CREATED_BY),
		"appName");
	Assert.assertEquals(roleBindingList.getItems().get(0).getSubjects().size(), 1);
	Assert.assertEquals(roleBindingList.getItems().get(0).getSubjects().get(0).getKind(), OnboardingService.USER);
	Assert.assertEquals(roleBindingList.getItems().get(0).getSubjects().get(0).getApiGroup(),
		OnboardingService.API_GROUP);
	Assert.assertEquals(roleBindingList.getItems().get(0).getSubjects().get(0).getName(), user.getId());
	Assert.assertEquals(roleBindingList.getItems().get(0).getSubjects().get(0).getNamespace(),
		"namespaceprefix-userprefix-id");
	Assert.assertEquals(roleBindingList.getItems().get(0).getRoleRef().getName(), OnboardingService.ADMIN);
	Assert.assertEquals(roleBindingList.getItems().get(0).getRoleRef().getApiGroup(), OnboardingService.API_GROUP);
	Assert.assertEquals(roleBindingList.getItems().get(0).getRoleRef().getKind(), OnboardingService.CLUSTER_ROLE);

    }

    @Test
    public void clusterCredentialTests() {
	final OnboardingService onBoardingService = new OnboardingService(this.clusterProperties, this.client);
	final ClusterCredentials clusterCredentials = onBoardingService.getClusterCredentials(user);
	Assert.assertEquals("authToken", clusterCredentials.getToken());
	Assert.assertEquals("apiserver-url", clusterCredentials.getApiserverUrl());
	Assert.assertEquals("namespaceprefix-userprefix-id", clusterCredentials.getNamespace());
	Assert.assertEquals("userprefix-id", clusterCredentials.getUser());
    }
	 *
	 */
}

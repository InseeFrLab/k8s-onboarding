package io.insee.dev.k8sonboarding.service;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.insee.dev.k8sonboarding.configuration.KubernetesClientProvider;
import io.insee.dev.k8sonboarding.configuration.properties.ClusterProperties;
import io.insee.dev.k8sonboarding.model.User;
import jdk.jfr.Description;

public class OnboardingServiceTests {
	@Mock
	KubernetesClientProvider kubernetesClientProvider = mock(KubernetesClientProvider.class);
	@Mock
	KubernetesClient kubernetesClient = mock(KubernetesClient.class);
	private ClusterProperties clusterProperties = new ClusterProperties();

	private User user = new User();

	private OnboardingService onboardingService;

	@BeforeEach
	public void setup() {
		Mockito.when(kubernetesClientProvider.getKubernetesClient()).thenReturn(kubernetesClient);
		this.onboardingService = new OnboardingService(clusterProperties, kubernetesClientProvider);
		onboardingService.setDoesRemoveSuffix(true);
		onboardingService.setKubernetesClient(kubernetesClient);
		clusterProperties.setNamespacePrefix("namespaceprefix-");
		clusterProperties.setUserPrefix("userprefix-");
		clusterProperties.setGroupPrefix("grpprefix-");
		clusterProperties.setNamespaceGroupPrefix("grpprefix-");
		clusterProperties.setApiserverUrl("api-server-url");
		final User user = new User();
		user.setId("id");
		user.setAuthToken("authToken");
	}

	/**
	 * 
	 * @Test
	 *       public void checkNamespaceExistsTest() {
	 *       //
	 *       mock.expect().withPath("/api/v1/namespaces/namespaceprefix-userprefix-id")
	 *       // .andReturn(200, new NamespaceBuilder().build()).once();
	 *       client.namespaces().createNew().withNewMetadata().withName("namespaceprefix-userprefix-id").endMetadata()
	 *       .done();
	 *       final OnboardingService onBoardingService = new
	 *       OnboardingService(this.clusterProperties, this.client);
	 *       Assert.assertEquals(Boolean.TRUE,
	 *       onBoardingService.checkNamespaceExists("namespaceprefix-userprefix-id"));
	 *       }
	 * 
	 * @Test
	 *       public void checkNamespaceDontExists() {
	 *       final OnboardingService onBoardingService = new
	 *       OnboardingService(this.clusterProperties, this.client);
	 *       Assert.assertEquals(Boolean.FALSE,
	 *       onBoardingService.checkNamespaceExists("namespaceprefix-userprefix-id"));
	 *       }
	 * 
	 * @Test
	 *       public void onboardTests() {
	 *       final OnboardingService onBoardingService = new
	 *       OnboardingService(this.clusterProperties, this.client);
	 *       ReflectionTestUtils.setField(onBoardingService, "appName", "appName");
	 *       //onBoardingService.onboard(user);
	 *       final Namespace namespace =
	 *       client.namespaces().withName("namespaceprefix-userprefix-id").get();
	 *       Assert.assertEquals(namespace.getMetadata().getLabels().size(), 1);
	 *       Assert.assertEquals(namespace.getMetadata().getLabels().get(OnboardingService.LABEL_CREATED_BY),
	 *       "appName");
	 *       final RoleBindingList roleBindingList = client.rbac().roleBindings()
	 *       .inNamespace("namespaceprefix-userprefix-id").list();
	 *       Assert.assertEquals(roleBindingList.getItems().size(), 1);
	 *       Assert.assertEquals(roleBindingList.getItems().get(0).getMetadata().getLabels().size(),
	 *       1);
	 *       Assert.assertEquals(
	 *       roleBindingList.getItems().get(0).getMetadata().getLabels().get(OnboardingService.LABEL_CREATED_BY),
	 *       "appName");
	 *       Assert.assertEquals(roleBindingList.getItems().get(0).getSubjects().size(),
	 *       1);
	 *       Assert.assertEquals(roleBindingList.getItems().get(0).getSubjects().get(0).getKind(),
	 *       OnboardingService.USER);
	 *       Assert.assertEquals(roleBindingList.getItems().get(0).getSubjects().get(0).getApiGroup(),
	 *       OnboardingService.API_GROUP);
	 *       Assert.assertEquals(roleBindingList.getItems().get(0).getSubjects().get(0).getName(),
	 *       user.getId());
	 *       Assert.assertEquals(roleBindingList.getItems().get(0).getSubjects().get(0).getNamespace(),
	 *       "namespaceprefix-userprefix-id");
	 *       Assert.assertEquals(roleBindingList.getItems().get(0).getRoleRef().getName(),
	 *       OnboardingService.ADMIN);
	 *       Assert.assertEquals(roleBindingList.getItems().get(0).getRoleRef().getApiGroup(),
	 *       OnboardingService.API_GROUP);
	 *       Assert.assertEquals(roleBindingList.getItems().get(0).getRoleRef().getKind(),
	 *       OnboardingService.CLUSTER_ROLE);
	 * 
	 *       }
	 * 
	 * @Test
	 *       public void clusterCredentialTests() {
	 *       final OnboardingService onBoardingService = new
	 *       OnboardingService(this.clusterProperties, this.client);
	 *       final ClusterCredentials clusterCredentials =
	 *       onBoardingService.getClusterCredentials(user);
	 *       Assert.assertEquals("authToken", clusterCredentials.getToken());
	 *       Assert.assertEquals("apiserver-url",
	 *       clusterCredentials.getApiserverUrl());
	 *       Assert.assertEquals("namespaceprefix-userprefix-id",
	 *       clusterCredentials.getNamespace());
	 *       Assert.assertEquals("userprefix-id", clusterCredentials.getUser());
	 *       }
	 *
	 */
	@Test()
	@Description("check that sanitization and removing suffix works on group namespace id")
	public void getNamespaceIdShouldBeOk() {
		String toSanitizeGroup = "observability_K8S-DEV";
		String namespaceId = onboardingService.getNamespaceId(user, toSanitizeGroup);
		var grpou = onboardingService.getGroupIdPrefixed(toSanitizeGroup);
		System.out.println(grpou);
		System.out.println(toSanitizeGroup);
		Assertions.assertEquals("grpprefix-observability", namespaceId);
	}
	/*
	 * @Test
	 * 
	 * @Description("check that role correspond to real kubernetes objects")
	 * public void addPermissionsToNamespaceTest(){
	 * String toSanitizeGroup = "observability_k8s-dev";
	 * var mockedNamespace = mock(NonNamespaceOperation.class);
	 * Mockito.when(mockedNamespace);
	 * Mockito.when(kubernetesClient.namespaces()).thenReturn(mockedNamespace);
	 * var roleBinding =
	 * onboardingService.addPermissionsToNamespace(user,toSanitizeGroup);
	 * }
	 */
}

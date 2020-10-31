package io.insee.dev.k8sonboarding.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

@Configuration
public class KubernetesClientConfiguration {

    @Bean
    public KubernetesClient kubernetesClient() {
	return new DefaultKubernetesClient();
    }

}

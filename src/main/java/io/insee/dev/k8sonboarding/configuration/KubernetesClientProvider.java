package io.insee.dev.k8sonboarding.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

@Service
public class KubernetesClientProvider {

    public KubernetesClient getKubernetesClient() {
        return new KubernetesClientBuilder().build();
    }

    @Bean
    public KubernetesClient kubernetesClient() {
        return new KubernetesClientBuilder().build();
    }

}

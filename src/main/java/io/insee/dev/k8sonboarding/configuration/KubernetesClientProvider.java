package io.insee.dev.k8sonboarding.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.stereotype.Service;

@Service
public class KubernetesClientProvider {

    public KubernetesClient getKubernetesClient() {
        return new DefaultKubernetesClient();
    }

    @Bean
    public KubernetesClient kubernetesClient() {
        return new DefaultKubernetesClient();
    }

}

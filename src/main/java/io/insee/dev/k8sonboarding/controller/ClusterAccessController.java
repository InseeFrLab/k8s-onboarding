package io.insee.dev.k8sonboarding.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cluster")
public class ClusterAccessController {

    @GetMapping
    public ClusterCredentials getCredentials() {
        return new ClusterCredentials();
    }

    public static class ClusterCredentials {
        private String apiserverUrl = "https://changeme";

        public String getApiserverUrl() {
            return apiserverUrl;
        }

        public void setApiserverUrl(String apiserverUrl) {
            this.apiserverUrl = apiserverUrl;
        }
    }
}

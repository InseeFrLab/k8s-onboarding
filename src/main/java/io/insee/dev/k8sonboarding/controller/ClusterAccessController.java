package io.insee.dev.k8sonboarding.controller;

import io.insee.dev.k8sonboarding.model.User;
import io.insee.dev.k8sonboarding.security.UserProvider;
import io.insee.dev.k8sonboarding.service.OnboardingService;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cluster")
public class ClusterAccessController {

    @Autowired
    private OnboardingService onboardingService;

    @Autowired
    private UserProvider userProvider;

    @Value("${cluster.apiserver-url}")
    private String apiServerUrl;

    @GetMapping
    public ClusterCredentials getCredentials() {
        ClusterCredentials credentials = new ClusterCredentials();
        credentials.setToken(userProvider.getUser().getAuthToken());
        credentials.setApiserverUrl(apiServerUrl);
        return credentials;
    }

    @PostMapping
    public void onboard() {
        onboardingService.onboard(userProvider.getUser());
    }

    public static class ClusterCredentials {
        private String apiserverUrl;
        private String token = null;


        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getApiserverUrl() {
            return apiserverUrl;
        }

        public void setApiserverUrl(String apiserverUrl) {
            this.apiserverUrl = apiserverUrl;
        }
    }
}

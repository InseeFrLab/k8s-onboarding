package io.insee.dev.k8sonboarding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.insee.dev.k8sonboarding.configuration.security.UserProvider;
import io.insee.dev.k8sonboarding.service.OnboardingService;

@RestController
@RequestMapping("/api/cluster")
@ConditionalOnProperty(prefix = "io.insee.dev.k8sonboarding.ui", name = "user-namespace-enabled", havingValue = "true")
public class UserAccessController {
    @Autowired
    private OnboardingService onboardingService;

    @Autowired
    private UserProvider userProvider;

    @PostMapping("/namespace")
    public void createNamespaceForUser(Authentication auth) {
        onboardingService.createNamespace(userProvider.getUser(auth), null);
    }

    @PostMapping("/permissions")
    public void addPermissionsForUser(Authentication auth) {
        onboardingService.addPermissionsToNamespace(userProvider.getUser(auth), null);
    }

}

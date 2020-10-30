package io.insee.dev.k8sonboarding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.insee.dev.k8sonboarding.security.UserProvider;
import io.insee.dev.k8sonboarding.service.OnboardingService;
import io.insee.dev.k8sonboarding.view.ClusterCredentials;

@RestController
@RequestMapping("/api/cluster")
public class ClusterAccessController {

    @Autowired
    private OnboardingService onboardingService;

    @Autowired
    private UserProvider userProvider;

    @GetMapping
    public ClusterCredentials getCredentials() {
	if (!onboardingService.checkNamespaceExists(userProvider.getUser())) {
	    onboardingService.onboard(userProvider.getUser());
	}
	return onboardingService.getClusterCredentials(userProvider.getUser());
    }

}

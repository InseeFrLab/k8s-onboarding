package io.insee.dev.k8sonboarding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.insee.dev.k8sonboarding.property.ClusterProperties;
import io.insee.dev.k8sonboarding.security.UserProvider;
import io.insee.dev.k8sonboarding.service.OnboardingService;

@RestController
@RequestMapping("/api/cluster")
public class ClusterAccessController {

    @Autowired
    private OnboardingService onboardingService;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ClusterProperties clusterProperty;

    @GetMapping
    public ClusterCredentials getCredentials() {
      final ClusterCredentials credentials = new ClusterCredentials();
      credentials.setToken(userProvider.getUser().getAuthToken());
      credentials.setApiserverUrl(clusterProperty.getApiserverUrl());
  	return credentials;
    }

    @PostMapping
    public void onboard() {
	if (!onboardingService.checkNamespaceExists(userProvider.getUser())) {
	    onboardingService.onboard(userProvider.getUser());
	}
	;
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

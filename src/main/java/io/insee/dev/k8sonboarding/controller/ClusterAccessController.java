package io.insee.dev.k8sonboarding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.insee.dev.k8sonboarding.configuration.security.UserProvider;
import io.insee.dev.k8sonboarding.model.User;
import io.insee.dev.k8sonboarding.service.OnboardingService;
import io.insee.dev.k8sonboarding.view.ClusterCredentials;

@RestController
@RequestMapping("/api/cluster")
public class ClusterAccessController {

    @Autowired
    private OnboardingService onboardingService;

    @Autowired
    private UserProvider userProvider;

    @Value("${io.insee.dev.k8sonboarding.jwt.username-claim}")
    private String usernameClaim;

    @GetMapping
    public ClusterCredentials getCredentials(Authentication auth) {
	return onboardingService.getClusterCredentials(userProvider.getUser(auth));
    }

    @PostMapping("/namespace/{namespaceId}")
    public void createNamespace(Authentication auth, @PathVariable String namespaceId) {
	onboardingService.createNamespace(userProvider.getUser(auth), namespaceId);
    }

    @PostMapping("/namespace/{namespaceId}/permissions")
    public void addPermissionsToNamespace(Authentication auth, @PathVariable String namespaceId) {
	onboardingService.addPermissionsToNamespace(userProvider.getUser(auth), namespaceId);
    }

    @Bean
    public UserProvider getUserProvider() {
	return auth -> {
	    final User user = new User();
	    final Jwt jwt = (Jwt) auth.getPrincipal();
	    user.setId(jwt.getClaimAsString(usernameClaim));
	    user.setAuthToken(jwt.getTokenValue());
	    return user;
	};
    }

}

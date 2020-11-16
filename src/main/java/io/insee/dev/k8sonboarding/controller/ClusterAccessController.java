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

    @Value("${io.insee.dev.k8sonboarding.jwt.groups-claim:groups}")
    private String groupsClaim;

    @GetMapping
    public ClusterCredentials getCredentials(Authentication auth) throws IllegalAccessException {
        return getCredentials(auth, null);
    }

    @GetMapping("/credentials/{groupId}")
    public ClusterCredentials getCredentials(Authentication auth, @PathVariable String groupId) throws IllegalAccessException {
	return onboardingService.getClusterCredentials(userProvider.getUser(auth), groupId);
    }

    @PostMapping("/namespace")
    public void createNamespaceForUser(Authentication auth) {
        onboardingService.createNamespace(userProvider.getUser(auth), null);
    }

    @PostMapping("/namespace/{groupId}")
    public void createNamespaceForGroup(Authentication auth, @PathVariable String groupId) {
	onboardingService.createNamespace(userProvider.getUser(auth), groupId);
    }

    @PostMapping("/permissions")
    public void addPermissionsForUser(Authentication auth) {
	onboardingService.addPermissionsToNamespace(userProvider.getUser(auth), null);
    }

    @PostMapping("/permissions/{groupId}")
    public void addPermissionsForUser(Authentication auth, @PathVariable String groupId) {
        onboardingService.addPermissionsToNamespace(userProvider.getUser(auth), groupId);
    }

    @Bean
    public UserProvider getUserProvider() {
	return auth -> {
	    final User user = new User();
	    final Jwt jwt = (Jwt) auth.getPrincipal();
	    user.setId(jwt.getClaimAsString(usernameClaim));
	    user.setGroups(jwt.getClaimAsStringList(groupsClaim));
	    user.setAuthToken(jwt.getTokenValue());
	    return user;
	};
    }

}

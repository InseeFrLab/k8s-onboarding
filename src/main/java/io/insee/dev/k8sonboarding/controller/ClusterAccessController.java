package io.insee.dev.k8sonboarding.controller;

import io.insee.dev.k8sonboarding.model.AllowedGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.insee.dev.k8sonboarding.configuration.security.UserProvider;
import io.insee.dev.k8sonboarding.service.OnboardingService;
import io.insee.dev.k8sonboarding.view.ClusterCredentials;

import java.util.List;

@RestController
@RequestMapping("/api/cluster")
public class ClusterAccessController {

    @Autowired
    private OnboardingService onboardingService;

    @Autowired
    private UserProvider userProvider;

    @GetMapping
    public ClusterCredentials getCredentials(Authentication auth) throws IllegalAccessException {
        return getCredentials(auth, null);
    }

    @GetMapping("/credentials/{groupId}")
    public ClusterCredentials getCredentials(Authentication auth, @PathVariable String groupId) throws IllegalAccessException {
        return onboardingService.getClusterCredentials(userProvider.getUser(auth), groupId);
    }

    @PostMapping("/namespace/{groupId}")
    public void createNamespaceForGroup(Authentication auth, @PathVariable String groupId) {
        onboardingService.createNamespace(userProvider.getUser(auth), groupId);
    }

    @PostMapping("/permissions/{groupId}")
    public void addPermissionsForUser(Authentication auth, @PathVariable String groupId) {
        onboardingService.addPermissionsToNamespace(userProvider.getUser(auth), groupId);
    }
    
    @GetMapping("/groups")
    public List<AllowedGroup> getAllowedGroupForUser(Authentication auth) {
        return onboardingService.getAllowedAndFilteredGroupsForUser(userProvider.getUser(auth));
    }

}

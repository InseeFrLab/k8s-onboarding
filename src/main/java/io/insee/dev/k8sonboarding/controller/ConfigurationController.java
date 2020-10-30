package io.insee.dev.k8sonboarding.controller;

import java.util.HashMap;
import java.util.Map;

import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/configuration")
public class ConfigurationController {

    @Autowired
    KeycloakSpringBootProperties keycloakSpringBootProperties;

    @GetMapping
    public Map<String, String> getConfiguration() {
	return this.mapPropertiesToPublish();
    }

    private Map<String, String> mapPropertiesToPublish() {
	final Map<String, String> map = new HashMap<>();
	map.put("auth", keycloakSpringBootProperties.getAuthServerUrl());
	map.put("realm", keycloakSpringBootProperties.getRealm());
	map.put("resource", keycloakSpringBootProperties.getResource());
	return map;
    }
}

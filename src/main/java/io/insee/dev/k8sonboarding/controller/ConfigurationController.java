package io.insee.dev.k8sonboarding.controller;

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
    public AppConfiguration getConfiguration() {
	final AppConfiguration appConfiguration = new AppConfiguration();
	appConfiguration.setAuthUrl(keycloakSpringBootProperties.getAuthServerUrl());
	appConfiguration.setRealm(keycloakSpringBootProperties.getRealm());
	appConfiguration.setResource(keycloakSpringBootProperties.getResource());
	return appConfiguration;
    }

    public static class AppConfiguration {
	private String authUrl;
	private String realm;
	private String resource;

	public AppConfiguration() {
	    super();
	}

	public String getAuthUrl() {
	    return authUrl;
	}

	public void setAuthUrl(String authUrl) {
	    this.authUrl = authUrl;
	}

	public String getRealm() {
	    return realm;
	}

	public void setRealm(String realm) {
	    this.realm = realm;
	}

	public String getResource() {
	    return resource;
	}

	public void setResource(String resource) {
	    this.resource = resource;
	}

    }

}

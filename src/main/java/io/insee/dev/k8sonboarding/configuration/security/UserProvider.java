package io.insee.dev.k8sonboarding.configuration.security;

import org.springframework.security.core.Authentication;

import io.insee.dev.k8sonboarding.model.User;

@FunctionalInterface
public interface UserProvider {

    public User getUser(Authentication auth);
}

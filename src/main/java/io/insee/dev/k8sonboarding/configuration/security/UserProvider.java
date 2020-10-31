package io.insee.dev.k8sonboarding.configuration.security;

import io.insee.dev.k8sonboarding.model.User;

@FunctionalInterface
public interface UserProvider {

    public User getUser();
}

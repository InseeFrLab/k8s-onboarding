package io.insee.dev.k8sonboarding.security;

import io.insee.dev.k8sonboarding.model.User;

@FunctionalInterface
public interface UserProvider {

    public User getUser();
}

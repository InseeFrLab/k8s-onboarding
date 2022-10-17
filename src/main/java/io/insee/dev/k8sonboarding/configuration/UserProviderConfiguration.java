package io.insee.dev.k8sonboarding.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.Jwt;

import io.insee.dev.k8sonboarding.configuration.security.UserProvider;
import io.insee.dev.k8sonboarding.model.User;

@Configuration
public class UserProviderConfiguration {

    @Value("${io.insee.dev.k8sonboarding.jwt.username-claim}")
    private String usernameClaim;

    @Value("${io.insee.dev.k8sonboarding.jwt.groups-claim:groups}")
    private String groupsClaim;

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
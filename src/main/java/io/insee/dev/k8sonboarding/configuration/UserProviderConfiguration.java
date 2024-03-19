package io.insee.dev.k8sonboarding.configuration;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import io.insee.dev.k8sonboarding.configuration.security.UserProvider;
import io.insee.dev.k8sonboarding.model.User;

@Configuration
public class UserProviderConfiguration {

    @Bean
    public UserProvider getUserProvider() {
        return auth -> {
            final User user = new User();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            user.setId(authentication.getName());
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(role -> role.replace("ROLE_", ""))
                    .collect(Collectors.toList());
            user.setGroups(roles);
            user.setAuthToken(((Jwt) auth.getPrincipal()).getTokenValue());
            return user;
        };
    }

}
package io.insee.dev.k8sonboarding.security;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.context.WebApplicationContext;

import io.insee.dev.k8sonboarding.model.User;

@KeycloakConfiguration
@EnableWebSecurity
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
	return new NullAuthenticatedSessionStrategy();
    }

    @Bean
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST)
    public AccessToken getAccessToken() {
	final AccessToken token = ((SimpleKeycloakAccount) SecurityContextHolder.getContext().getAuthentication()
		.getDetails()).getKeycloakSecurityContext().getToken();
	return token;
    }

    public String getAccessTokenString() {
	final String token = ((SimpleKeycloakAccount) SecurityContextHolder.getContext().getAuthentication()
		.getDetails()).getKeycloakSecurityContext().getTokenString();
	return token;
    }

    @Bean
    public UserProvider getUserProvider() {
	return () -> {
	    final AccessToken token = getAccessToken();
	    final User user = new User();
	    user.setId(token.getPreferredUsername());
	    user.setAuthToken(getAccessTokenString());
	    return user;
	};
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.httpBasic().disable().csrf().disable()
		.addFilterBefore(keycloakAuthenticationProcessingFilter(), X509AuthenticationFilter.class)
		.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).and()
		.addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class).authorizeRequests()
			.antMatchers("/api/public/**").permitAll()
		.antMatchers("/api/**").authenticated()
		.anyRequest().permitAll();
    }

    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
	return new KeycloakSpringBootConfigResolver();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	final KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
	// simple Authority Mapper to avoid ROLE_
	keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
	auth.authenticationProvider(keycloakAuthenticationProvider);
    }
}
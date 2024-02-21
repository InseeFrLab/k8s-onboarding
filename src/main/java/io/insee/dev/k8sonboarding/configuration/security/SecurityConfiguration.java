package io.insee.dev.k8sonboarding.configuration.security;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Value("${io.insee.dev.k8sonboarding.jwt.username-claim:preferred_username}")
    private String usernameClaim;

    @Value("${io.insee.dev.k8sonboarding.jwt.groups-claim:groups}")
    private String groupsClaim;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy())
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        http.authorizeHttpRequests(this::authorizedUrls);
        return http.build();
    }

    private void authorizedUrls(
            AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize) {
        String[] publicUrls = { "/api/public/**", "/api", "/api/" };
        String[] restrictedUrls = { "/api/**" };
        for (String url : publicUrls) {
            authorize.requestMatchers(antMatcher(url)).permitAll();
        }
        for (String url : restrictedUrls) {
            authorize.requestMatchers(antMatcher(url)).authenticated();
        }
        authorize.anyRequest().permitAll();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        jwtAuthenticationConverter.setPrincipalClaimName(usernameClaim);
        return jwtAuthenticationConverter;
    }

    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        return new Converter<Jwt, Collection<GrantedAuthority>>() {
            @Override
            @SuppressWarnings({ "unchecked" })
            public Collection<GrantedAuthority> convert(Jwt source) {
                String oidcClaimRole = groupsClaim;
                String[] claimPath = oidcClaimRole.split("\\.");
                Map<String, Object> claims = source.getClaims();
                try {
                    for (int i = 0; i < claimPath.length - 1; i++) {
                        claims = (Map<String, Object>) claims.get(claimPath[i]);
                    }
                    if (claims == null) {
                        return Collections.emptyList();
                    }
                    List<String> roles = (List<String>) claims
                            .getOrDefault(claimPath[claimPath.length - 1], Collections.emptyList());
                    return roles.stream()
                            .map(s -> new GrantedAuthority() {
                                @Override
                                public String getAuthority() {
                                    return "ROLE_" + s;
                                }

                                @Override
                                public String toString() {
                                    return getAuthority();
                                }
                            })
                            .collect(Collectors.toList());
                } catch (ClassCastException e) {
                    // role path not correctly found, assume that no role for this user
                    return Collections.emptyList();
                }
            }
        };
    }

}

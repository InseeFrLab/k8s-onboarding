package io.insee.dev.k8sonboarding.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable().csrf().disable()
        .authorizeRequests()
            .antMatchers("/api/public/**").permitAll()
            .antMatchers("/api","/api/").permitAll() // For swagger-ui redirection
            .antMatchers("/api/**").authenticated()
            .anyRequest().permitAll()
        .and()
            .oauth2ResourceServer()
            .jwt();
        return http.build();
    }

}    
  
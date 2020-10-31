package io.insee.dev.k8sonboarding.configuration.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.httpBasic().disable().csrf().disable()
	    .authorizeRequests()
			.antMatchers("/api/public/**").permitAll()
			.antMatchers("/api","/api/").permitAll() // For swagger-ui redirection
		    .antMatchers("/api/**").authenticated()
		    .anyRequest().permitAll()
		.and()
			.oauth2ResourceServer()
			.jwt();
	}
	

	
  
}
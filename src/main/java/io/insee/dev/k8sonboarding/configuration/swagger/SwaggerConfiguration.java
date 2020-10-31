package io.insee.dev.k8sonboarding.configuration.swagger;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfiguration {

    @Value("${io.insee.dev.k8sonboarding.swagger.oauth2.authUrl}")
    public String authUrl;

    @Value("${io.insee.dev.k8sonboarding.swagger.oauth2.tokenUrl}")
    public String tokenUrl;

    public final String SCHEME = "oAuthScheme";

    @Bean
    public OpenAPI customOpenAPIOauth2() {
	final OpenAPI openapi = createOpenAPI();
	openapi.components(new Components().addSecuritySchemes(SCHEME, new SecurityScheme()
		.type(SecurityScheme.Type.OAUTH2).in(SecurityScheme.In.HEADER).description("Authentification Oauth2")
		.flows(new OAuthFlows().authorizationCode(new OAuthFlow()
			.authorizationUrl(authUrl)
			.tokenUrl(tokenUrl)))));
	return openapi;
    }

    private OpenAPI createOpenAPI() {
	final OpenAPI openapi = new OpenAPI().info(new Info().title("OnBoarding-api")
		.description("This api allow to get credentials to access to a kubernetes cluster")
		.contact(new Contact().name("InseeFrLab").url("https://github.com/InseeFrLab/k8s-onboarding")));
	return openapi;
    }

    // permet d'ajouter le header Authorization aux header qui vont bien, ici on
    // l'ajoute que au methode qui ne sont pas sur /api/public/** */
    @Bean
    public OperationCustomizer addSecurity() {
	return (operation, handlerMethod) -> {
	    return operation.addSecurityItem(new SecurityRequirement().addList(SCHEME));
	};
    }
}

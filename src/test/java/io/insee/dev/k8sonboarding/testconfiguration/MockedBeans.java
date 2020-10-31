package io.insee.dev.k8sonboarding.testconfiguration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
public class MockedBeans {

    @MockBean
    private JwtDecoder jwtDecoder;

}

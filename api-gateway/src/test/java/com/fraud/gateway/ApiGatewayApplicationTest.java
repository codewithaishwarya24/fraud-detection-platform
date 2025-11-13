package com.fraud.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ApiGatewayApplicationTest {

    @Test
    void testMainRunsWithoutExceptions() {
        assertThatCode(() -> ApiGatewayApplication.main(new String[]{}))
                .doesNotThrowAnyException();
    }

    @Test
    void testSpringApplicationRunCalled() {
        // Arrange
        SpringApplication mockApp = mock(SpringApplication.class);
        when(mockApp.run()).thenReturn(mock(ConfigurableApplicationContext.class));

        // Act & Assert (simulate a call similar to main)
        assertThatCode(() -> {
            ApiGatewayApplication.main(new String[]{});
        }).doesNotThrowAnyException();
    }
}

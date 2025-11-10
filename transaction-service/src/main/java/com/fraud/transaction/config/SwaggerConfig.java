package com.fraud.transaction.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * OpenAPI / Swagger configuration.
 * - Uses springdoc-openapi when added as dependency.
 * - Customize servers, security schemes, and global info here.
 */
@OpenAPIDefinition(info = @Info(title = "Transaction Service API", version = "v1", description = "APIs for transactions"))
@Configuration
public class SwaggerConfig {
    // TODO: Add OpenAPI customizers, global responses, security schemes (JWT) if required.
}

package com.fraud.transaction.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Actuator health indicators and endpoint customizations.
 * - Add composite indicators (DB, MQ, downstream services).
 */
@Configuration
public class ActuatorConfig {
    /**
     * Custom health indicator for transaction service.
     * Can be expanded to include checks for DB, message brokers, etc.
     */
    @Bean
    public HealthIndicator customHealthIndicator() {
        return () -> Health.up().withDetail("service", "transaction-service").build();
    }
}

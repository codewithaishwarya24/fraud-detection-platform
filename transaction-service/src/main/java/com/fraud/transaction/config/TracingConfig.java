package com.fraud.transaction.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tracing & metrics configuration.
 * - Wire Micrometer common tags and (optionally) OpenTelemetry exporter configuration here.
 * - Ensure dependencies for micrometer-registry-prometheus and opentelemetry/jaeger are added in pom.
 */
@Configuration
public class TracingConfig {
    /**
     * Customize Micrometer MeterRegistry to add common tags.
     */
    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("service", "transaction-service");
    }

    // TODO: Configure OpenTelemetry/Jaeger exporters, sampling, and resource attributes.
}

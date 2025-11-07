package com.fraud.transaction.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign client configuration:
 * - Set log level, encoder/decoder, contract, request interceptors here.
 * - Use separate configuration per client when you need different timeouts or logging.
 */
@Configuration
public class FeignConfig {
    /**
     * Configure Feign client logging level.
     * Adjust level based on needs: NONE, BASIC, HEADERS, FULL.
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        // NONE, BASIC, HEADERS, FULL
        return Logger.Level.BASIC;
    }

    // TODO: add feign encoder/decoder beans, request interceptors for auth headers, and timeouts.
}

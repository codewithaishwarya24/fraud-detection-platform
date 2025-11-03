package com.fraud.transaction.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Logging related configuration:
 * - Register filters that populate MDC (correlation id, user id)
 * - Configure appender patterns in logback-spring.xml (not here)
 */
@Configuration
public class LoggingConfig {

}

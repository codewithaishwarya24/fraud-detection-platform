package com.fraud.transaction.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Optional database configuration.
 * - Use this if you need to customize DataSource, EntityManagerFactory or transaction manager.
 * - For most cases Spring Boot auto-configuration is sufficient.
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {
    // TODO: Define DataSource bean if you need custom pooling (HikariCP properties) or multi-db support.
}

package com.fraud.transaction.config;

import org.springframework.context.annotation.Configuration;

/**
 * Audit configuration placeholder:
 * - Configure AOP aspects or interceptors to capture audit events (who, what, when)
 * - Do not log PII; store minimal audit events with references.
 */
@Configuration
public class AuditConfig {
    // TODO: Wire AuditAspect and AuditService here to persist audit events to an audit store.
}

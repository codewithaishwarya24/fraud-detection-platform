package com.fraud.transaction.config;

import org.springframework.context.annotation.Configuration;

/**
 * Rate limiter configuration placeholder.
 * - Implement Bucket4j or Resilience4j rate limiting for global/per-route limits.
 * - Consider rate limits per API key/client, and burst handling.
 */
@Configuration
public class RateLimiterConfig {
    // TODO: Add beans to configure rate limiters and integrate with controller advice or filters.
}

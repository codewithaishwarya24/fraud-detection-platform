package com.fraud.common.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiErrorTest {
    @Test
    void testNoArgsConstructorAndSetters() {
        ApiError error = new ApiError();
        Instant now = Instant.now();
        Map<String, String> details = new HashMap<>();
        details.put("field", "invalid");

        error.setTimestamp(now);
        error.setStatus(400);
        error.setError("Bad Request");
        error.setMessage("Invalid input");
        error.setPath("/api/test");
        error.setDetails(details);

        assertThat(error.getTimestamp()).isEqualTo(now);
        assertThat(error.getStatus()).isEqualTo(400);
        assertThat(error.getError()).isEqualTo("Bad Request");
        assertThat(error.getMessage()).isEqualTo("Invalid input");
        assertThat(error.getPath()).isEqualTo("/api/test");
        assertThat(error.getDetails()).containsEntry("field", "invalid");
    }

    @Test
    void testAllArgsConstructor() {
        Instant now = Instant.now();
        Map<String, String> details = Map.of("param", "missing");
        ApiError error = new ApiError(now, 404, "Not Found", "Resource missing", "/api/item", details);

        assertThat(error.getStatus()).isEqualTo(404);
        assertThat(error.getError()).isEqualTo("Not Found");
        assertThat(error.getMessage()).isEqualTo("Resource missing");
        assertThat(error.getPath()).isEqualTo("/api/item");
        assertThat(error.getDetails()).containsEntry("param", "missing");
    }

    @Test
    void testBuilder_WithCustomValues() {
        Instant now = Instant.now();
        Map<String, String> details = Map.of("field", "error");

        ApiError error = ApiError.builder()
                .timestamp(now)
                .status(500)
                .error("Internal Server Error")
                .message("Unexpected issue")
                .path("/api/internal")
                .details(details)
                .build();

        assertThat(error.getTimestamp()).isEqualTo(now);
        assertThat(error.getStatus()).isEqualTo(500);
        assertThat(error.getError()).isEqualTo("Internal Server Error");
        assertThat(error.getMessage()).isEqualTo("Unexpected issue");
        assertThat(error.getPath()).isEqualTo("/api/internal");
        assertThat(error.getDetails()).containsEntry("field", "error");
    }

    @Test
    void testBuilder_DefaultTimestampApplied() {
        ApiError error = ApiError.builder()
                .status(400)
                .error("Bad Request")
                .message("Invalid payload")
                .path("/api/data")
                .build();

        assertThat(error.getTimestamp()).isNotNull(); // ✅ @Builder.Default applied
    }

    @Test
    void testEqualsAndHashCodeAndToString() {
        Instant now = Instant.now();
        Map<String, String> details = Map.of("key", "value");

        ApiError e1 = new ApiError(now, 401, "Unauthorized", "Auth failed", "/api/auth", details);
        ApiError e2 = new ApiError(now, 401, "Unauthorized", "Auth failed", "/api/auth", details);

        assertThat(e1).isEqualTo(e2);
        assertThat(e1.hashCode()).isEqualTo(e2.hashCode());
        assertThat(e1.toString()).contains("Unauthorized").contains("/api/auth");
    }
}

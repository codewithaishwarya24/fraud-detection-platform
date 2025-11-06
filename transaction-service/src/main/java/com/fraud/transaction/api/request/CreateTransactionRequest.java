package com.fraud.transaction.api.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request payload for creating a new transaction.
 * Client supplies only input fields. System-generated fields (transactionId, createdAt, etc.)
 * are not part of this request.
 */
@Data
@AllArgsConstructor
@Builder
public class CreateTransactionRequest {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(max = 3, message = "Currency must be a valid 3-letter ISO code")
    private String currency;

    @NotBlank(message = "Merchant ID is required")
    private String merchantId;

    @Size(max = 32, message = "Card number masked value too long")
    private String cardNumberMasked;

    private String cardType;

    @NotBlank(message = "Transaction type is required")
    private String transactionType;

    private String responseCode;

    @Min(value = 0, message = "Risk score cannot be negative")
    @Max(value = 100, message = "Risk score cannot exceed 100")
    private Integer riskScore;

    private String channel;

    @Pattern(
            regexp = "^$|^((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])$",
            message = "Invalid IP address format"
    )
    private String ipAddress;

    private String deviceId;
    private String location;

    /**
     * Optional: allow client to send a transactionTime if recorded client-side.
     * If absent, service should set LocalDateTime.now().
     */
    @PastOrPresent(message = "transactionTime cannot be in the future")
    private LocalDateTime transactionTime;

    /**
     * Optional idempotency key for safe retries.
     */
    @Size(max = 128, message = "Idempotency key too long")
    private String idempotencyKey;
}

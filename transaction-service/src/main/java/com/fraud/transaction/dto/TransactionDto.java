package com.fraud.transaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for transactions. This is intended for server -> client responses.
 * Fields that are system-managed are marked read-only (ignored during deserialization).
 */
@Data
@AllArgsConstructor
@Builder
public class TransactionDto {

    /**
     * Server-generated transaction id — read-only for clients.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String transactionId;

    private BigDecimal amount;
    private String currency;
    private String merchantId;
    private String cardNumberMasked;
    private String cardType;
    private String transactionType;
    private String responseCode;

    /**
     * Whether the transaction is flagged. Read-only for clients.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isFlagged;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String flagReason;

    private Integer riskScore;
    private String reviewStatus;
    private String channel;
    private String ipAddress;
    private String deviceId;
    private String location;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime transactionTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime flaggedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String flaggedBy;
}

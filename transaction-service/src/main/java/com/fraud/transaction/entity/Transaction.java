package com.fraud.transaction.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a financial transaction.
 *
 * This maps directly to the "transactions" table in the database.
 * Includes audit timestamps and flagging metadata for fraud analysis.
 */
@Entity
@Table(name = "transactions") // plural table name (convention)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Business-level unique transaction identifier.
     */
    @Column(name = "transaction_id", nullable = false, unique = true, updatable = false, length = 64)
    private String transactionId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(length = 3, nullable = false)
    private String currency;

    @Column(name = "merchant_id", nullable = false)
    private String merchantId;

    @Column(name = "card_number_masked", length = 32)
    private String cardNumberMasked;

    @Column(name = "card_type")
    private String cardType;

    @Column(name = "transaction_type", nullable = false)
    private String transactionType;

    @Column(name = "response_code")
    private String responseCode;

    @Column(name = "is_flagged", nullable = false)
    private Boolean isFlagged = Boolean.FALSE;

    @Column(name = "flag_reason")
    private String flagReason;

    @Column(name = "flagged_at")
    private LocalDateTime flaggedAt;

    @Column(name = "flagged_by")
    private String flaggedBy;

    @Column(name = "risk_score")
    private Integer riskScore;

    @Column(name = "review_status")
    private String reviewStatus;

    @Column(name = "channel")
    private String channel;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "device_id", length = 128)
    private String deviceId;

    private String location;

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

    /**
     * Automatically managed creation timestamp.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Automatically managed last update timestamp.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

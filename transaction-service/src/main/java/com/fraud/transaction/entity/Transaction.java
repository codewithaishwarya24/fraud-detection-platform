package com.fraud.transaction.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String transactionId;

    private BigDecimal amount;
    private String currency;
    private String merchantId;
    private String cardNumberMasked;
    private String cardType;
    private String transactionType;
    private String responseCode;

    private Boolean isFlagged;
    private String flagReason;
    private LocalDateTime flaggedAt;
    private String flaggedBy;

    private Integer riskScore;
    private String reviewStatus;
    private String channel;
    private String ipAddress;
    private String deviceId;
    private String location;

    private LocalDateTime transactionTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

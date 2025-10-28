package com.fraud.transaction.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
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
    private Integer riskScore;
    private String reviewStatus;
    private String channel;
    private String ipAddress;
    private String deviceId;
    private String location;
    private LocalDateTime transactionTime;
    private LocalDateTime createdAt;
}

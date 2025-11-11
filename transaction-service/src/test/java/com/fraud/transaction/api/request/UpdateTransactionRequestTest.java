package com.fraud.transaction.api.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateTransactionRequestTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidation_whenAllFieldsAreValid() {
        UpdateTransactionRequest request = UpdateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100.0))
                .currency("USD")
                .merchantId("MAR101")
                .cardNumberMasked("**** **** **** 0012")
                .cardType("VISA")
                .transactionType("PURCHASE")
                .responseCode("00")
                .isFlagged(false)
                .flagReason("Suspicious pattern")
                .riskScore(50)
                .reviewStatus("APPROVED")
                .channel("WEB")
                .ipAddress("192.168.0.1")
                .deviceId("device-001")
                .location("Pune")
                .transactionTime(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<UpdateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_whenAmountIsNullOrNegative() {
        UpdateTransactionRequest request = UpdateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(-10))
                .currency("USD")
                .merchantId("MAR101")
                .transactionType("PURCHASE")
                .build();

        Set<ConstraintViolation<UpdateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Amount must be positive")));
    }

    @Test
    void shouldFailValidation_whenCurrencyIsBlankOrTooLong() {
        UpdateTransactionRequest request = UpdateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .currency("USDA")
                .merchantId("MAR101")
                .transactionType("PURCHASE")
                .build();

        Set<ConstraintViolation<UpdateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency must be a valid 3-letter ISO code")));
    }

    @Test
    void shouldFailValidation_whenMerchantIdIsBlank() {
        UpdateTransactionRequest request = UpdateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .merchantId(" ")
                .transactionType("PURCHASE")
                .build();

        Set<ConstraintViolation<UpdateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Merchant ID is required")));
    }

    @Test
    void shouldFailValidation_whenTransactionTypeIsMissing() {
        UpdateTransactionRequest request = UpdateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .merchantId("MAR101")
                .transactionType("")
                .build();

        Set<ConstraintViolation<UpdateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Transaction type is required")));
    }

    @Test
    void shouldFailValidation_whenRiskScoreIsOutOfRange() {
        UpdateTransactionRequest request = UpdateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .merchantId("MAR101")
                .transactionType("PURCHASE")
                .riskScore(150)
                .build();

        Set<ConstraintViolation<UpdateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Risk score cannot exceed 100")));
    }

    @Test
    void shouldFailValidation_whenIpAddressIsInvalid() {
        UpdateTransactionRequest request = UpdateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .merchantId("MAR101")
                .transactionType("PURCHASE")
                .ipAddress("999.999.999.999")
                .build();

        Set<ConstraintViolation<UpdateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Invalid IP address format")));
    }

    @Test
    void shouldFailValidation_whenTransactionTimeIsInFuture() {
        UpdateTransactionRequest request = UpdateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .merchantId("MAR101")
                .transactionType("PURCHASE")
                .transactionTime(LocalDateTime.now().plusDays(1))
                .build();

        Set<ConstraintViolation<UpdateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("transactionTime cannot be in the future")));
    }

    @Test
    void shouldFailValidation_whenFlagReasonIsTooLong() {
        String longReason = "x".repeat(513);
        UpdateTransactionRequest request = UpdateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .merchantId("MAR101")
                .transactionType("PURCHASE")
                .flagReason(longReason)
                .build();

        Set<ConstraintViolation<UpdateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Flag reason too long")));
    }
}
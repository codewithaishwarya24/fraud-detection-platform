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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateTransactionRequestTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidation_whenAllFieldsAreValid() {
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100.0))
                .currency("USD")
                .merchantId("MAR101")
                .cardNumberMasked("**** **** **** 0012")
                .cardType("VISA")
                .transactionType("PURCHASE")
                .riskScore(50)
                .ipAddress("192.168.1.1")
                .transactionTime(LocalDateTime.now())
                .idempotencyKey("abc-123-idempotent")
                .build();

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldFailValidation_whenAmountIsNullOrNegative() {
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(-10))
                .currency("USD")
                .merchantId("MAR101")
                .transactionType("PURCHASE")
                .build();

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Amount must be positive")));
    }

    @Test
    void shouldFailValidation_whenCurrencyIsBlankOrTooLong() {
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .currency("USDA")
                .merchantId("MAR101")
                .transactionType("PURCHASE")
                .build();

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Currency must be a valid 3-letter ISO code")));
    }

    @Test
    void shouldFailValidation_whenMerchantIdIsBlank() {
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .merchantId(" ")
                .transactionType("PURCHASE")
                .build();

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Merchant ID is required")));
    }

    @Test
    void shouldFailValidation_whenTransactionTypeIsMissing() {
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .merchantId("MAR101")
                .transactionType("")
                .build();

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Transaction type is required")));
    }

    @Test
    void shouldFailValidation_whenRiskScoreIsOutOfRange() {
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .merchantId("MAR101")
                .transactionType("PURCHASE")
                .riskScore(150)
                .build();

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Risk score cannot exceed 100")));
    }

    @Test
    void shouldFailValidation_whenIpAddressIsInvalid() {
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .merchantId("MAR101")
                .transactionType("PURCHASE")
                .ipAddress("999.999.999.999")
                .build();

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> getMessage(v).contains("Invalid IP address format")));
    }

    private static String getMessage(ConstraintViolation<CreateTransactionRequest> v) {
        return v.getMessage();
    }

    @Test
    void shouldFailValidation_whenTransactionTimeIsInFuture() {
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .merchantId("MAR101")
                .transactionType("PURCHASE")
                .transactionTime(LocalDateTime.now().plusDays(1))
                .build();

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("transactionTime cannot be in the future")));
    }

    @Test
    void shouldFailValidation_whenIdempotencyKeyIsTooLong() {
        String longKey = "x".repeat(129);
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100))
                .currency("USD")
                .merchantId("MAR101")
                .transactionType("PURCHASE")
                .idempotencyKey(longKey)
                .build();

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Idempotency key too long")));
    }
}

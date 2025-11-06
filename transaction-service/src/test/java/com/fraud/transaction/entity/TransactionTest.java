package com.fraud.transaction.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Transaction tx = new Transaction();
        tx.setId(1L);
        tx.setTransactionId("TXN123");
        tx.setAmount(BigDecimal.valueOf(999.99));
        tx.setCurrency("INR");
        tx.setMerchantId("M123");
        tx.setCardNumberMasked("XXXX-XXXX-XXXX-1234");
        tx.setCardType("VISA");
        tx.setTransactionType("PURCHASE");
        tx.setResponseCode("00");
        tx.setIsFlagged(true);
        tx.setFlagReason("Suspicious IP");
        tx.setFlaggedAt(LocalDateTime.now());
        tx.setFlaggedBy("System");
        tx.setRiskScore(85);
        tx.setReviewStatus("PENDING");
        tx.setChannel("WEB");
        tx.setIpAddress("192.168.1.1");
        tx.setDeviceId("DEVICE-001");
        tx.setLocation("Mumbai");
        tx.setTransactionTime(LocalDateTime.now());

        assertEquals("TXN123", tx.getTransactionId());
        assertEquals("INR", tx.getCurrency());
        assertTrue(tx.getIsFlagged());
        assertEquals("System", tx.getFlaggedBy());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Transaction tx = new Transaction(
                1L, "TXN456", BigDecimal.valueOf(500.00), "USD", "M456",
                "XXXX-XXXX-XXXX-5678", "MASTERCARD", "REFUND", "01", false,
                "Manual review", now, "Analyst", 60, "REVIEWED", "MOBILE",
                "10.0.0.1", "DEVICE-XYZ", "Delhi", now, now, now
        );

        assertEquals("TXN456", tx.getTransactionId());
        assertEquals("USD", tx.getCurrency());
        assertEquals("M456", tx.getMerchantId());
        assertEquals("REFUND", tx.getTransactionType());
        assertEquals("Analyst", tx.getFlaggedBy());
    }

    @Test
    void testBuilder() {
        Transaction tx = Transaction.builder()
                .transactionId("TXN789")
                .amount(BigDecimal.valueOf(250.00))
                .currency("EUR")
                .merchantId("M789")
                .transactionType("WITHDRAWAL")
                .isFlagged(false)
                .build();

        assertEquals("TXN789", tx.getTransactionId());
        assertEquals(BigDecimal.valueOf(250.00), tx.getAmount());
        assertEquals("EUR", tx.getCurrency());
        assertEquals("WITHDRAWAL", tx.getTransactionType());
        assertFalse(tx.getIsFlagged());
    }

    @Test
    void testDefaultIsFlaggedValue() {
        Transaction tx = new Transaction();
        assertFalse(tx.getIsFlagged(), "Default value of isFlagged should be false");
    }
}
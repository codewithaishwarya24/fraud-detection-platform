package com.fraud.transaction.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MerchantClientFallbackTest {
    private MerchantClientFallback merchantClientFallback;

    @BeforeEach
    void setUp() {
        merchantClientFallback = new MerchantClientFallback();
    }

    @Test
    void testGetMerchantById_ReturnsFallbackResponse() {
        // Arrange
        String merchantId = "M001";

        // Act
        MerchantResponse response = merchantClientFallback.getMerchantById(merchantId);

        // Assert
        assertNotNull(response);
        assertEquals(merchantId, response.getMerchantId());
        assertEquals("UNKNOWN", response.getName());
        assertEquals("N/A", response.getLocation());
        assertEquals("N/A", response.getCategory());
    }

    @Test
    void testGetMerchantById_WithNullMerchantId() {
        // Act
        MerchantResponse response = merchantClientFallback.getMerchantById(null);

        // Assert
        assertNotNull(response);
        assertNull(response.getMerchantId());
        assertEquals("UNKNOWN", response.getName());
        assertEquals("N/A", response.getLocation());
        assertEquals("N/A", response.getCategory());
    }
}

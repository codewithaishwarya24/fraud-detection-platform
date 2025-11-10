package com.fraud.transaction.integration;

import org.springframework.stereotype.Component;

@Component
public class MerchantClientFallback implements MerchantClient {

    @Override
    public MerchantResponse getMerchantById(String merchantId) {
        // Return minimal fallback response
        return MerchantResponse.builder()
                .merchantId(merchantId)
                .name("UNKNOWN")
                .location("N/A")
                .category("N/A")
                .build();
    }
}
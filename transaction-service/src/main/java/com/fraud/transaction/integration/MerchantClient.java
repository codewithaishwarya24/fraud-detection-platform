package com.fraud.transaction.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "merchant-service", fallback = MerchantClientFallback.class)
public interface MerchantClient {
    @GetMapping("/api/merchants/id/{merchantId}")
    MerchantResponse getMerchantById(@PathVariable("merchantId") String merchantId);
}

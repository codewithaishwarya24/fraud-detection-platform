package com.fraud.transaction.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantResponse {
    private String merchantId;
    private String name;
    private String location;
    private String category;
}

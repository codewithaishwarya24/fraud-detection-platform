package com.fraud.merchant.model;

import lombok.Data;

@Data
public class MerchantDTO {
    private String merchantId;
    private String name;
    private String acquirerName;
    private String category;
    private String domain;
    private String country;
    private String city;
    private Boolean isHighRisk;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}

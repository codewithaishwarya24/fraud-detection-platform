package com.fraud.merchant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "merchant")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true)
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

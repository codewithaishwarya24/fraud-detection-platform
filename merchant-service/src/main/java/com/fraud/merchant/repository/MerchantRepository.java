package com.fraud.merchant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fraud.merchant.entity.Merchant;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {}

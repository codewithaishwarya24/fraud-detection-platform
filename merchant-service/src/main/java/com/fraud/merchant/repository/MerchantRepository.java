package com.fraud.merchant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fraud.merchant.entity.Merchant;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    boolean existsByMerchantId(String merchantId);

    List<Merchant> findMerchantsById(Long id);
}

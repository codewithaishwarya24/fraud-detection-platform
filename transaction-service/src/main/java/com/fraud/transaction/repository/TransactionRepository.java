package com.fraud.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fraud.transaction.entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFlaggedTrue();

    List<Transaction> findByMerchantId(Long merchantId);

}

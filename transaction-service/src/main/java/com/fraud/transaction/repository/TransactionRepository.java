package com.fraud.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fraud.transaction.entity.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByIsFlaggedTrue();


    List<Transaction> findByMerchantId(String merchantId);


}

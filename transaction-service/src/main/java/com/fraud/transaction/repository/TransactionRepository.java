package com.fraud.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fraud.transaction.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {}

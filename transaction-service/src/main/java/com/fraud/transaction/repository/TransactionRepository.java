package com.fraud.transaction.repository;

import com.fraud.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find all transactions that are flagged as suspicious.
     */
    List<Transaction> findByIsFlaggedTrue();

    /**
     * Find all transactions for a given merchant.
     *
     * @param merchantId merchant identifier
     * @return list of transactions
     */
    List<Transaction> findByMerchantId(String merchantId);

    /**
     * Find a transaction by its business transaction ID (not the database ID).
     *
     * @param transactionId unique transaction ID
     * @return Optional of Transaction (empty if not found)
     */
    Optional<Transaction> findByTransactionId(String transactionId);
}

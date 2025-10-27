package com.fraud.transaction.service;

import com.fraud.transaction.entity.Transaction;
import com.fraud.transaction.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    // PATCH: Flag a transaction as suspicious
    public Transaction flagTransaction(Long id, String comment) {
        Transaction txn = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));

        txn.setIsFlagged(true);
        return transactionRepository.save(txn);
    }

    // GET: All flagged transactions
    public List<Transaction> getFlaggedTransactions() {
        return transactionRepository.findByFlaggedTrue();
    }

    // GET: All transactions by merchant
    public List<Transaction> getTransactionsByMerchant(Long merchantId) {
        return transactionRepository.findByMerchantId(merchantId);
    }
}

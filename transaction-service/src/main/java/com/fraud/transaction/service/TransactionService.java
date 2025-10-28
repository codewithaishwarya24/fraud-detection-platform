package com.fraud.transaction.service;

import com.fraud.transaction.entity.Transaction;
import com.fraud.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    public TransactionRepository repository;

    public Optional<Transaction> getTransactionById(Long id) {
        return repository.findById(id);
    }

    public Optional<Transaction> updateTransaction(Long id, Transaction updatedTransaction) {
        return repository.findById(id)
                .map(existing -> {
                    if (updatedTransaction.getTransactionId() != null)
                        existing.setTransactionId(updatedTransaction.getTransactionId());

                    existing.setAmount(updatedTransaction.getAmount());
                    existing.setCurrency(updatedTransaction.getCurrency());
                    existing.setMerchantId(updatedTransaction.getMerchantId());
                    existing.setCardNumberMasked(updatedTransaction.getCardNumberMasked());
                    existing.setCardType(updatedTransaction.getCardType());
                    existing.setTransactionType(updatedTransaction.getTransactionType());
                    existing.setResponseCode(updatedTransaction.getResponseCode());
                    existing.setIsFlagged(updatedTransaction.getIsFlagged());
                    existing.setFlagReason(updatedTransaction.getFlagReason());
                    existing.setRiskScore(updatedTransaction.getRiskScore());
                    existing.setReviewStatus(updatedTransaction.getReviewStatus());
                    existing.setChannel(updatedTransaction.getChannel());
                    existing.setIpAddress(updatedTransaction.getIpAddress());
                    existing.setDeviceId(updatedTransaction.getDeviceId());
                    existing.setLocation(updatedTransaction.getLocation());
                    existing.setTransactionTime(updatedTransaction.getTransactionTime());
                    existing.setCreatedAt(updatedTransaction.getCreatedAt());

                    return repository.save(existing);
                });
    }
}

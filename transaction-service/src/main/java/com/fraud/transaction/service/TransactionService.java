package com.fraud.transaction.service;

import com.fraud.transaction.entity.Transaction;
import com.fraud.transaction.model.TransactionDto;
import com.fraud.transaction.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper = Mappers.getMapper(TransactionMapper.class);

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public Optional<Transaction> updateTransaction(Long id, Transaction updatedTransaction) {
        return transactionRepository.findById(id)
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

                    return transactionRepository.save(existing);
                });
    }

    public ResponseEntity<String> createTransaction(TransactionDto transactionDto) {
        try {
            Transaction transaction = transactionMapper.mapTransactionDtoToTransaction(transactionDto);
            transaction.setCreatedAt(LocalDateTime.now());
            transaction.setTransactionTime(LocalDateTime.now());
            transactionRepository.save(transaction);
            return new ResponseEntity<>("Transaction created successfully", HttpStatus.CREATED);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>("Failed to create transaction", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<TransactionDto>> getAllTransaction() {
        List<Transaction> transactionList = transactionRepository.findAll();
        return new ResponseEntity<>(transactionMapper.toTransactionDtoList(transactionList), HttpStatus.OK);
    }

    // Flag a transaction as suspicious
    public Transaction flagTransaction(Long id, String comment) {
        Transaction txn = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));
        txn.setIsFlagged(true);
        return transactionRepository.save(txn);
    }

    //All flagged transactions
    public List<Transaction> getFlaggedTransactions() {
        return transactionRepository.findByIsFlaggedTrue();
    }

    //All transactions by merchant
    public List<Transaction> getTransactionsByMerchant(String merchantId){
                return transactionRepository.findByMerchantId(merchantId);
            }

}

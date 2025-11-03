package com.fraud.transaction.service;

import com.fraud.transaction.entity.Transaction;
import com.fraud.transaction.model.FlagTransactionRequest;
import com.fraud.transaction.model.TransactionDto;
import com.fraud.transaction.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionMapper transactionMapper = Mappers.getMapper(TransactionMapper.class);

    public ResponseEntity<TransactionDto> getTransactionById(String transId) {
        Transaction transaction = transactionRepository.findByTransactionId(transId);
        if (null!=transaction)
            return new ResponseEntity<>(transactionMapper.mapTransactionToTransactionDto(transaction), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<TransactionDto> updateTransaction(String transactionId, TransactionDto updatedTransaction) {
        Transaction existing = transactionRepository.findByTransactionId(transactionId);
                if(null!=existing) {
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
                    transactionRepository.save(existing);
                    return new ResponseEntity<>(transactionMapper.mapTransactionToTransactionDto(existing),HttpStatus.OK);
                }
                else
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);

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

    public Transaction flagTransaction(String transactionId, FlagTransactionRequest request) {
        Transaction txn = Optional.of(transactionRepository.findByTransactionId(transactionId))
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with transaction id: " + transactionId));

        txn.setIsFlagged(true);
        txn.setFlagReason(request.getComment());
        txn.setUpdatedAt(LocalDateTime.now());
        txn.setFlaggedAt(LocalDateTime.now());
        txn.setFlaggedBy("system");

        return transactionRepository.save(txn);
    }

    public List<Transaction> getFlaggedTransactions() {
        List<Transaction> flaggedTransactions = transactionRepository.findByIsFlaggedTrue();
        if (flaggedTransactions.isEmpty()) {
            log.info("No flagged transactions found.");
        }
        return flaggedTransactions;
    }

    public List<Transaction> getTransactionsByMerchant(String merchantId) {
        if (merchantId == null || merchantId.trim().isEmpty()) {
            throw new IllegalArgumentException("Merchant ID must not be null or empty.");
        }
        
        List<Transaction> merchantTransactions = transactionRepository.findByMerchantId(merchantId);
        if (merchantTransactions.isEmpty()) {
            log.info("No transactions found for merchant ID: {}", merchantId);
        }
        return merchantTransactions;
    }

}

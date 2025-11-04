package com.fraud.transaction.controller;

import com.fraud.transaction.api.request.CreateTransactionRequest;
import com.fraud.transaction.api.request.UpdateTransactionRequest;
import com.fraud.transaction.dto.TransactionDto;
import com.fraud.transaction.api.request.FlagTransactionRequest;
import com.fraud.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Validated
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Health check endpoint.
     */
    @GetMapping("/health")
    public String health() {
        return "Transaction service running OK";
    }

    /**
     * Fetch a transaction by its ID.
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable String transactionId) {
        log.debug("Fetching transaction with ID: {}", transactionId);
        TransactionDto dto = transactionService.getTransactionById(transactionId);
        log.info("Fetched transaction {} successfully", dto.getTransactionId());
        return ResponseEntity.ok(dto);
    }

    /**
     * Update an existing transaction.
     */
    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> updateTransaction(
            @PathVariable String transactionId,
            @Valid @RequestBody UpdateTransactionRequest updatedTransaction) {

        log.debug("Updating transaction {}", transactionId);
        TransactionDto updated = transactionService.updateTransaction(transactionId, updatedTransaction);
        log.info("Updated transaction {} successfully", transactionId);
        return ResponseEntity.ok(updated);
    }

    /**
     * Create a new transaction.
     * Returns 201 Created with Location header.
     */
    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody CreateTransactionRequest transactionDto) {
        log.debug("Creating transaction for merchantId={}", transactionDto.getMerchantId());

        TransactionDto created = transactionService.createTransaction(transactionDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{transactionId}")
                .buildAndExpand(created.getTransactionId())
                .toUri();

        log.info("Created transaction with ID={} for merchant={}", created.getTransactionId(), created.getMerchantId());
        return ResponseEntity.created(location).body(created);
    }

    /**
     * Fetch all transactions.
     */
    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        log.debug("Fetching all transactions");
        List<TransactionDto> list = transactionService.getAllTransactions();
        log.info("Returning {} transactions", list.size());
        return ResponseEntity.ok(list);
    }

    /**
     * Flag a transaction as suspicious.
     */
    @PatchMapping("/{transactionId}/flag")
    public ResponseEntity<TransactionDto> flagTransaction(
            @PathVariable String transactionId,
            @Valid @RequestBody FlagTransactionRequest request) {

        log.debug("Flagging transaction {}", transactionId);
        TransactionDto flagged = transactionService.flagTransaction(transactionId, request);
        log.info("Transaction {} flagged successfully", transactionId);

        return ResponseEntity.ok(flagged);
    }

    /**
     * Fetch all flagged transactions.
     */
    @GetMapping("/flagged")
    public ResponseEntity<List<TransactionDto>> getFlaggedTransactions() {
        log.debug("Fetching all flagged transactions");
        List<TransactionDto> flagged = transactionService.getFlaggedTransactions();
        log.info("Returning {} flagged transactions", flagged.size());
        return ResponseEntity.ok(flagged);
    }

    /**
     * Fetch transactions by merchant ID.
     */
    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<TransactionDto>> getByMerchant(@PathVariable String merchantId) {
        log.debug("Fetching transactions for merchant {}", merchantId);
        List<TransactionDto> list = transactionService.getTransactionsByMerchant(merchantId);
        log.info("Returning {} transactions for merchant {}", list.size(), merchantId);
        return ResponseEntity.ok(list);
    }
}

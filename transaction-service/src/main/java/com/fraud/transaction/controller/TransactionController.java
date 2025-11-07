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

/**
 * This class is a Spring Boot REST controller for managing transactions.
 * It provides endpoints for creating, updating, fetching, and flagging transactions.
 *
 * The controller handles HTTP requests and delegates the business logic to the
 * `TransactionService`. It uses annotations to define request mappings and
 * validation rules.
 *
 * Key features:
 * - Health check endpoint.
 * - CRUD operations for transactions.
 * - Fetching flagged transactions.
 * - Fetching transactions by merchant ID.
 **/
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
     * This endpoint is used to verify that the transaction service is running and operational.
     * It returns a simple string message indicating the service status.
     *
     * @return A string message "Transaction service running OK".
     */
    @GetMapping("/health")
    public String health() {
        return "Transaction service running OK";
    }

    /**
     * Fetch a transaction by its ID.
     *
     * <p>Delegates to {@code TransactionService#getTransactionById(String)} to load the domain
     * object and maps it to {@code TransactionDto} returned inside a {@code ResponseEntity}.</p>
     *
     * @param transactionId the identifier of the transaction to retrieve; must not be null or empty
     * @return a {@code ResponseEntity} containing the {@code TransactionDto} for the given id
     * @see com.fraud.transaction.service.TransactionService#getTransactionById(String)
     * @see com.fraud.transaction.dto.TransactionDto
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
     *
     * <p>Validates the incoming {@code UpdateTransactionRequest}, delegates the update to the
     * service layer and returns the updated {@code TransactionDto} wrapped in a {@code ResponseEntity}.</p>
     *
     * @param transactionId the identifier of the transaction to update
     * @param updatedTransaction the request payload containing updated transaction fields; validated with Jakarta Validation
     * @return a {@code ResponseEntity} containing the updated {@code TransactionDto}
     * @see com.fraud.transaction.api.request.UpdateTransactionRequest
     * @see com.fraud.transaction.service.TransactionService#updateTransaction(String, com.fraud.transaction.api.request.UpdateTransactionRequest)
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
     *
     * <p>Validates the incoming {@code CreateTransactionRequest}, creates the transaction via the
     * service, and returns a 201 Created response with the created {@code TransactionDto} and a
     * Location header pointing to the new resource.</p>
     *
     * @param transactionDto the request payload used to create a new transaction; validated with Jakarta Validation
     * @return a {@code ResponseEntity} with status 201 Created containing the created {@code TransactionDto}
     * @see com.fraud.transaction.api.request.CreateTransactionRequest
     * @see com.fraud.transaction.service.TransactionService#createTransaction(com.fraud.transaction.api.request.CreateTransactionRequest)
     * @see org.springframework.web.servlet.support.ServletUriComponentsBuilder
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
     *
     * <p>Returns a list of all transactions as {@code TransactionDto} objects wrapped in a
     * {@code ResponseEntity}.</p>
     *
     * @return a {@code ResponseEntity} containing a {@code List} of {@code TransactionDto}
     * @see com.fraud.transaction.service.TransactionService#getAllTransactions()
     * @see com.fraud.transaction.dto.TransactionDto
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
     *
     * <p>flags a specified transaction using the details in {@code FlagTransactionRequest},
     * delegating to the service layer and returning the flagged {@code TransactionDto}.</p>
     *
     * @param transactionId the identifier of the transaction to flag
     * @param request the flagging request containing reason/metadata; validated with Jakarta Validation
     * @return a {@code ResponseEntity} containing the flagged {@code TransactionDto}
     * @see com.fraud.transaction.api.request.FlagTransactionRequest
     * @see com.fraud.transaction.service.TransactionService#flagTransaction(String, com.fraud.transaction.api.request.FlagTransactionRequest)
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
     *
     * <p>Returns all transactions that have been marked as flagged/suspicious.</p>
     *
     * @return a {@code ResponseEntity} containing a {@code List} of flagged {@code TransactionDto}
     * @see com.fraud.transaction.service.TransactionService#getFlaggedTransactions()
     * @see com.fraud.transaction.dto.TransactionDto
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
     *
     * <p>Returns transactions associated with the provided merchant identifier.</p>
     *
     * @param merchantId the merchant identifier used to filter transactions
     * @return a {@code ResponseEntity} containing a {@code List} of {@code TransactionDto} for the merchant
     * @see com.fraud.transaction.service.TransactionService#getTransactionsByMerchant(String)
     * @see com.fraud.transaction.dto.TransactionDto
     */
    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<TransactionDto>> getByMerchant(@PathVariable String merchantId) {
        log.debug("Fetching transactions for merchant {}", merchantId);
        List<TransactionDto> list = transactionService.getTransactionsByMerchant(merchantId);
        log.info("Returning {} transactions for merchant {}", list.size(), merchantId);
        return ResponseEntity.ok(list);
    }
}

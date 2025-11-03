package com.fraud.transaction.controller;


import com.fraud.transaction.entity.Transaction;
import com.fraud.transaction.model.FlagTransactionRequest;
import com.fraud.transaction.model.TransactionDto;
import com.fraud.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    private final TransactionService transactionService;

    @GetMapping("/health")
    public String health() {
        return "Transaction service running OK";
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable("transactionId") String transactionId) {
        return transactionService.getTransactionById(transactionId);
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> updateTransaction(@PathVariable("transactionId") String transactionId,
                                                         @RequestBody TransactionDto updatedTransaction) {

        return transactionService.updateTransaction(transactionId, updatedTransaction);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionService.createTransaction(transactionDto);
    }

    @GetMapping("/allTransaction")
    public ResponseEntity<List<TransactionDto>> getAllTransaction(){
        return transactionService.getAllTransaction();
    }

    @PatchMapping("/{id}/flag")
    public ResponseEntity<Transaction> flagTransaction(@PathVariable("id") Long id,
                                                       @Valid @RequestBody FlagTransactionRequest request) {
        Transaction updated = transactionService.flagTransaction(id, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/flagged")
    public ResponseEntity<List<Transaction>> getFlaggedTransactions() {
        return ResponseEntity.ok(transactionService.getFlaggedTransactions());
    }

    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<Transaction>> getByMerchant(@PathVariable("merchantId") String merchantId) {
        return ResponseEntity.ok(transactionService.getTransactionsByMerchant(merchantId));
    }

}

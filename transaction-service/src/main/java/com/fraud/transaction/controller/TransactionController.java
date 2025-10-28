package com.fraud.transaction.controller;


import com.fraud.transaction.entity.Transaction;
import com.fraud.transaction.model.TransactionDto;
import com.fraud.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/health")
    public String health() {
        return "Transaction service running OK";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") Long id) {
        Optional<Transaction> optionalTransaction = transactionService.getTransactionById(id);
        return optionalTransaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable("id") Long id,
                                                         @RequestBody Transaction updatedTransaction) {

        return transactionService.updateTransaction(id, updatedTransaction)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionService.createTransaction(transactionDto);
    }

    @GetMapping("/allTransaction")
    public ResponseEntity<List<TransactionDto>> getAllTransaction(){
        return transactionService.getAllTransaction();
    }

    // PATCH /api/transactions/{id}/flag
    @PatchMapping("/{id}/flag")
    public ResponseEntity<Transaction> flagTransaction(@PathVariable Long id,
                                                       @RequestBody Map<String, String> request) {
        String comment = request.get("comment");
        Transaction updated = transactionService.flagTransaction(id, comment);
        return ResponseEntity.ok(updated);
    }

    // GET /api/transactions/flagged
    @GetMapping("/flagged")
    public ResponseEntity<List<Transaction>> getFlaggedTransactions() {
        return ResponseEntity.ok(transactionService.getFlaggedTransactions());
    }

    // GET /api/transactions/merchant/{merchantId}
    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<Transaction>> getByMerchant(@PathVariable Long merchantId) {
        return ResponseEntity.ok(transactionService.getTransactionsByMerchant(merchantId));
    }

}

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

    @PatchMapping("/{transactionId}/flag")
    public ResponseEntity<TransactionDto> flagTransaction(@PathVariable("transactionId") String transactionId,
                                                       @Valid @RequestBody FlagTransactionRequest request) {
        return transactionService.flagTransaction(transactionId, request);

    }

    @GetMapping("/flagged")
    public ResponseEntity<List<TransactionDto>> getFlaggedTransactions() {
        return transactionService.getFlaggedTransactions();
    }

    @GetMapping("/merchant/{merchantId}")
    public ResponseEntity<List<TransactionDto>> getByMerchant(@PathVariable("merchantId") String merchantId) {
        return transactionService.getTransactionsByMerchant(merchantId);
    }

}

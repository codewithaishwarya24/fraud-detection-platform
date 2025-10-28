package com.fraud.transaction.controller;

import com.fraud.transaction.entity.Transaction;
import com.fraud.transaction.model.TransactionDto;
import com.fraud.transaction.repository.TransactionRepository;
import com.fraud.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    public TransactionService service;

    @GetMapping("/health")
    public String health() {
        return "Transaction service running OK";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("id") Long id){
        Optional<Transaction> optionalTransaction = service.getTransactionById(id);
        return optionalTransaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable("id") Long id,
            @RequestBody Transaction updatedTransaction) {

        return service.updateTransaction(id, updatedTransaction)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTransaction(@RequestBody TransactionDto transactionDto) {
        return service.createTransaction(transactionDto);
    }

    @GetMapping("/allTransaction")
    public ResponseEntity<List<TransactionDto>> getAllTransaction(){
        return service.getAllTransaction();
    }
}

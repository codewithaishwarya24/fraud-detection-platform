package com.fraud.transaction;

import com.fraud.transaction.api.request.CreateTransactionRequest;
import com.fraud.transaction.dto.TransactionDto;
import com.fraud.transaction.entity.Transaction;
import com.fraud.transaction.mapper.TransactionMapper;
import com.fraud.transaction.repository.TransactionRepository;
import com.fraud.transaction.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class TransactionServiceApplicationTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void contextLoads() {
        // This test ensures that the Spring application context starts without errors.
    }

    @Test
    void testCreateTransactionSuccess() {

        // Input request
        CreateTransactionRequest request = new CreateTransactionRequest();

        // Entity before saving (mapped)
        Transaction entity = new Transaction();
        entity.setTransactionId(null); // test UUID generation
        entity.setIsFlagged(null);
        entity.setCreatedAt(null);
        entity.setTransactionTime(null);

        // Entity after saving (repository response)
        Transaction saved = new Transaction();
        saved.setTransactionId("generated-uuid-123");
        saved.setIsFlagged(false);
        saved.setCreatedAt(LocalDateTime.now());
        saved.setTransactionTime(LocalDateTime.now());

        // DTO response
        TransactionDto dto = new TransactionDto();
        dto.setTransactionId("generated-uuid-123");

        Mockito.when(transactionMapper.fromCreateRequest(request)).thenReturn(entity);
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(saved);
        Mockito.when(transactionMapper.toDto(saved)).thenReturn(dto);

        // Execute
        TransactionDto result = transactionService.createTransaction(request);

        // Verify save was called
        Mockito.verify(transactionRepository).save(Mockito.any(Transaction.class));

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertEquals("generated-uuid-123", result.getTransactionId());
    }
}
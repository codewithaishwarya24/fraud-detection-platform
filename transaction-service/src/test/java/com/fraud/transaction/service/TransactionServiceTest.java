package com.fraud.transaction.service;

import com.fraud.transaction.api.request.CreateTransactionRequest;
import com.fraud.transaction.api.request.UpdateTransactionRequest;
import com.fraud.transaction.dto.TransactionDto;
import com.fraud.transaction.entity.Transaction;
import com.fraud.transaction.mapper.TransactionMapper;
import com.fraud.transaction.repository.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldReturnTransactionDto_whenValidIdProvided() {
        // Arrange
        String transactionId = "tx123";
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setMerchantId("MAR101");

        TransactionDto expectedDto = TransactionDto.builder()
                .transactionId(transactionId)
                .merchantId("MAR101")
                .build();

        Mockito.when(transactionRepository.findByTransactionId(transactionId))
                .thenReturn(Optional.of(transaction));
        Mockito.when(transactionMapper.toDto(transaction)).thenReturn(expectedDto);

        // Act
        TransactionDto result = transactionService.getTransactionById(transactionId);

        // Assert
        assertNotNull(result);
        assertEquals(transactionId, result.getTransactionId());
        assertEquals("MAR101", result.getMerchantId());
    }

    @Test
    void shouldThrowIllegalArgumentException_whenTransactionIdIsBlank() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.getTransactionById(" ")
        );

        assertEquals("transactionId must not be null or blank", exception.getMessage());
    }

    @Test
    void shouldThrowEntityNotFoundException_whenTransactionNotFound() {
        // Arrange
        String transactionId = "tx999";
        Mockito.when(transactionRepository.findByTransactionId(transactionId))
                .thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                transactionService.getTransactionById(transactionId)
        );

        assertEquals("Transaction not found with id: tx999", exception.getMessage());
    }

    @Test
    void shouldCreateTransactionSuccessfully() {
        // Arrange
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(125.25))
                .currency("USD")
                .merchantId("MAR101")
                .cardNumberMasked("**** **** **** 0012")
                .transactionType("PURCHASE")
                .riskScore(10)
                .build();

        Transaction entity = new Transaction();
        entity.setAmount(request.getAmount());
        entity.setCurrency(request.getCurrency());
        entity.setMerchantId(request.getMerchantId());
        entity.setCardNumberMasked(request.getCardNumberMasked());
        entity.setTransactionType(request.getTransactionType());
        entity.setRiskScore(request.getRiskScore());
        // transactionId, createdAt, transactionTime, isFlagged will be set in service

        Transaction savedEntity = new Transaction();
        savedEntity.setTransactionId("tx123");
        savedEntity.setAmount(entity.getAmount());
        savedEntity.setCurrency(entity.getCurrency());
        savedEntity.setMerchantId(entity.getMerchantId());
        savedEntity.setCardNumberMasked(entity.getCardNumberMasked());
        savedEntity.setTransactionType(entity.getTransactionType());
        savedEntity.setRiskScore(entity.getRiskScore());
        savedEntity.setIsFlagged(Boolean.FALSE);
        savedEntity.setCreatedAt(LocalDateTime.now());
        savedEntity.setTransactionTime(LocalDateTime.now());

        TransactionDto expectedDto = TransactionDto.builder()
                .transactionId("tx123")
                .amount(savedEntity.getAmount())
                .currency(savedEntity.getCurrency())
                .merchantId(savedEntity.getMerchantId())
                .cardNumberMasked(savedEntity.getCardNumberMasked())
                .transactionType(savedEntity.getTransactionType())
                .riskScore(savedEntity.getRiskScore())
                .build();

        Mockito.when(transactionMapper.fromCreateRequest(request)).thenReturn(entity);
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(savedEntity);
        Mockito.when(transactionMapper.toDto(savedEntity)).thenReturn(expectedDto);

        // Act
        TransactionDto result = transactionService.createTransaction(request);

        // Assert
        assertNotNull(result);
        assertEquals("tx123", result.getTransactionId());
        assertEquals("MAR101", result.getMerchantId());
        assertEquals(BigDecimal.valueOf(125.25), result.getAmount());
        assertEquals("USD", result.getCurrency());
    }

    @Test
    void shouldThrowException_whenCreateRequestIsNull() {
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                transactionService.createTransaction(null)
        );
        assertEquals("createRequest must not be null", exception.getMessage());
    }

    @Test
    void shouldUpdateTransactionSuccessfully() {
        // Arrange
        String transactionId = "tx123";
        UpdateTransactionRequest request = UpdateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(200.0))
                .merchantId("MAR101")
                .currency("USD")
                .build();

        Transaction existing = new Transaction();
        existing.setTransactionId(transactionId);
        existing.setAmount(BigDecimal.valueOf(150.0));
        existing.setMerchantId("MAR101");
        existing.setCurrency("USD");

        Transaction updated = new Transaction();
        updated.setTransactionId(transactionId);
        updated.setAmount(request.getAmount());
        updated.setMerchantId(request.getMerchantId());
        updated.setCurrency(request.getCurrency());
        updated.setUpdatedAt(LocalDateTime.now());
        TransactionDto expectedDto = TransactionDto.builder()
                .transactionId(transactionId)
                .amount(request.getAmount())
                .merchantId(request.getMerchantId())
                .currency(request.getCurrency())
                .build();

        Mockito.when(transactionRepository.findByTransactionId(transactionId)).thenReturn(Optional.of(existing));
        Mockito.doAnswer(invocation -> {
            // Simulate in-place update
            Transaction target = invocation.getArgument(1);
            target.setAmount(request.getAmount());
            return null;
        }).when(transactionMapper).updateFromUpdateRequest(request, existing);
        Mockito.when(transactionRepository.save(existing)).thenReturn(updated);
        Mockito.when(transactionMapper.toDto(updated)).thenReturn(expectedDto);

        // Act
        TransactionDto result = transactionService.updateTransaction(transactionId, request);

        // Assert
        assertNotNull(result);
        assertEquals(transactionId, result.getTransactionId());
        assertEquals(BigDecimal.valueOf(200.0), result.getAmount());
        assertEquals("MAR101", result.getMerchantId());
        assertEquals("USD", result.getCurrency());
    }

    @Test
    void shouldThrowIllegalArgumentException_whenUpdateTransactionIdIsBlank() {
        UpdateTransactionRequest request = UpdateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100.0))
                .merchantId("MAR101")
                .currency("USD")
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.updateTransaction(" ", request)
        );

        assertEquals("transactionId must not be null or blank", exception.getMessage());
    }

    @Test
    void shouldThrowNullPointerException_whenRequestIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                transactionService.updateTransaction("tx123", null)
        );

        assertEquals("updatedRequest must not be null", exception.getMessage());
    }

    @Test
    void shouldThrowEntityNotFoundException_whenTransactionDoesNotExist() {
        String transactionId = "tx999";
        UpdateTransactionRequest request = UpdateTransactionRequest.builder()
                .amount(BigDecimal.valueOf(100.0))
                .merchantId("MAR101")
                .currency("USD")
                .build();

        Mockito.when(transactionRepository.findByTransactionId(transactionId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                transactionService.updateTransaction(transactionId, request)
        );

        assertEquals("Transaction not found with id: tx999", exception.getMessage());
    }

    @Test
    void shouldReturnAllTransactions() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setTransactionId("tx001");
        transaction.setMerchantId("MAR101");
        transaction.setAmount(BigDecimal.valueOf(250.0));
        transaction.setCurrency("USD");

        List<Transaction> entityList = List.of(transaction);

        TransactionDto dto = TransactionDto.builder()
                .transactionId("tx001")
                .merchantId("MAR101")
                .amount(BigDecimal.valueOf(250.0))
                .currency("USD")
                .build();

        List<TransactionDto> dtoList = List.of(dto);

        Mockito.when(transactionRepository.findAll()).thenReturn(entityList);
        Mockito.when(transactionMapper.toDtoList(entityList)).thenReturn(dtoList);
        // Act
        List<TransactionDto> result = transactionService.getAllTransactions();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("tx001", result.get(0).getTransactionId());
        assertEquals("MAR101", result.get(0).getMerchantId());
        assertEquals(BigDecimal.valueOf(250.0), result.get(0).getAmount());
        assertEquals("USD", result.get(0).getCurrency());
    }

}

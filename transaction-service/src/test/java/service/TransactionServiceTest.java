package service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Collections;
import java.util.Optional;

import com.fraud.transaction.api.request.FlagTransactionRequest;
import com.fraud.transaction.dto.TransactionDto;
import com.fraud.transaction.entity.Transaction;
import com.fraud.transaction.mapper.TransactionMapper;
import com.fraud.transaction.repository.TransactionRepository;
import com.fraud.transaction.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StringUtils;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionService transactionService;

    private FlagTransactionRequest request;

    private Transaction transaction1;
    private Transaction transaction2;
    private TransactionDto dto1;
    private TransactionDto dto2;

    @BeforeEach
    void setup() {
        transaction1 = new Transaction();
        transaction1.setId(1290L);
        transaction1.setIsFlagged(false);
        transaction1.setFlaggedAt(null);
        transaction1.setFlagReason(null);
        transaction1.setIsFlagged(true);

        request = new FlagTransactionRequest();
        request.setComment("Suspicious amount");

        transaction2 = new Transaction();
        transaction2.setId(12912L);
        transaction2.setIsFlagged(true);

        dto1 = new TransactionDto();
        dto1.setTransactionId("1290L");
        dto1.setIsFlagged(true);
        dto2 = new TransactionDto();
        dto2.setTransactionId("12912L");
    }

    // -------------------------------------------------------
    // Tests for getFlaggedTransactions()
    // -------------------------------------------------------

    @Test
    void testGetFlaggedTransactions_ReturnsList_WhenFlaggedTransactionsExist() {
        // Arrange
        List<Transaction> flaggedList = List.of(transaction1, transaction2);
        when(transactionRepository.findByIsFlaggedTrue()).thenReturn(flaggedList);
        when(transactionMapper.toDtoList(flaggedList)).thenReturn(List.of(dto1, dto2));

        // Act
        List<TransactionDto> result = transactionService.getFlaggedTransactions();

        // Assert
        assertEquals(2, result.size());
        assertEquals("1290L", result.get(0).getTransactionId());
        verify(transactionRepository, times(1)).findByIsFlaggedTrue();
        verify(transactionMapper, times(1)).toDtoList(flaggedList);
    }

    @Test
    void testGetFlaggedTransactions_ReturnsEmptyList_WhenNoFlaggedTransactionsFound() {
        // Arrange
        when(transactionRepository.findByIsFlaggedTrue()).thenReturn(Collections.emptyList());
        when(transactionMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Act
        List<TransactionDto> result = transactionService.getFlaggedTransactions();

        // Assert
        assertTrue(result.isEmpty());
        verify(transactionRepository, times(1)).findByIsFlaggedTrue();
        verify(transactionMapper, times(1)).toDtoList(Collections.emptyList());
    }

    // -------------------------------------------------------
    // Tests for getTransactionsByMerchant()
    // -------------------------------------------------------

    @Test
    void testGetTransactionsByMerchant_ReturnsList_WhenTransactionsExist() {
        // Arrange
        String merchantId = "merchant-123";
        List<Transaction> transactions = List.of(transaction1);
        when(transactionRepository.findByMerchantId(merchantId)).thenReturn(transactions);
        when(transactionMapper.toDtoList(transactions)).thenReturn(List.of(dto1));

        // Act
        List<TransactionDto> result = transactionService.getTransactionsByMerchant(merchantId);

        // Assert
        assertEquals(1, result.size());
        assertEquals("1290L", result.get(0).getTransactionId());
        verify(transactionRepository, times(1)).findByMerchantId(merchantId);
        verify(transactionMapper, times(1)).toDtoList(transactions);
    }

    @Test
    void testGetTransactionsByMerchant_ReturnsEmptyList_WhenNoTransactionsFound() {
        // Arrange
        String merchantId = "merchant-123";
        when(transactionRepository.findByMerchantId(merchantId)).thenReturn(Collections.emptyList());
        when(transactionMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Act
        List<TransactionDto> result = transactionService.getTransactionsByMerchant(merchantId);

        // Assert
        assertTrue(result.isEmpty());
        verify(transactionRepository, times(1)).findByMerchantId(merchantId);
        verify(transactionMapper, times(1)).toDtoList(Collections.emptyList());
    }

    @Test
    void testGetTransactionsByMerchant_ThrowsException_WhenMerchantIdIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.getTransactionsByMerchant(null));

        assertEquals("Merchant ID must not be null or empty.", exception.getMessage());
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(transactionMapper);
    }

    @Test
    void testGetTransactionsByMerchant_ThrowsException_WhenMerchantIdIsEmpty() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.getTransactionsByMerchant(""));

        assertEquals("Merchant ID must not be null or empty.", exception.getMessage());
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(transactionMapper);
    }

    @Test
    void testGetTransactionsByMerchant_ThrowsException_WhenMerchantIdIsBlank() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.getTransactionsByMerchant("   "));

        assertEquals("Merchant ID must not be null or empty.", exception.getMessage());
        verifyNoInteractions(transactionRepository);
        verifyNoInteractions(transactionMapper);
    }

    @Test
    void testFlagTransaction_SuccessfullyFlagsTransaction() {
        // Arrange
        when(transactionRepository.findByTransactionId("txn-123"))
                .thenReturn(Optional.of(transaction1));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // simulate save
        when(transactionMapper.toDto(any(Transaction.class)))
                .thenReturn(dto1);

        // Act
        TransactionDto result = transactionService.flagTransaction("txn-123", request);

        // Assert
        assertNotNull(result);
        assertEquals("1290L", result.getTransactionId());
        assertTrue(transaction1.getIsFlagged());
        assertEquals("Suspicious amount", transaction1.getFlagReason());
        assertEquals("system", transaction1.getFlaggedBy());
        assertNotNull(transaction1.getFlaggedAt());
        assertNotNull(transaction1.getUpdatedAt());

        verify(transactionRepository, times(1)).findByTransactionId("txn-123");
        verify(transactionRepository, times(1)).save(transaction1);
        verify(transactionMapper, times(1)).toDto(transaction1);
    }


    @Test
    void testFlagTransaction_ThrowsException_WhenTransactionIdIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.flagTransaction(null, request));

        assertEquals("transactionId must not be null or blank", exception.getMessage());
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testFlagTransaction_ThrowsException_WhenTransactionIdIsBlank() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transactionService.flagTransaction("   ", request));

        assertEquals("transactionId must not be null or blank", exception.getMessage());
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void testFlagTransaction_ThrowsException_WhenRequestIsNull() {
        // Act + Assert
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> transactionService.flagTransaction("txn-123", null));

        assertEquals("request must not be null", exception.getMessage());
        verifyNoInteractions(transactionRepository);
    }


    @Test
    void testFlagTransaction_ThrowsException_WhenTransactionNotFound() {
        // Arrange
        when(transactionRepository.findByTransactionId("txn-404"))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> transactionService.flagTransaction("txn-404", request));

        assertEquals("Transaction not found with id: txn-404", exception.getMessage());
        verify(transactionRepository, times(1)).findByTransactionId("txn-404");
        verify(transactionRepository, never()).save(any());
    }


    @Test
    void testFlagTransaction_UpdatesEntityBeforeSave() {
        // Arrange
        when(transactionRepository.findByTransactionId("txn-123"))
                .thenReturn(Optional.of(transaction1));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionMapper.toDto(any(Transaction.class)))
                .thenReturn(dto1);

        // Act
        transactionService.flagTransaction("txn-123", request);

        // Assert that all flag-related fields were set
        assertTrue(transaction1.getIsFlagged());
        assertEquals("Suspicious amount", transaction1.getFlagReason());
        assertEquals("system", transaction1.getFlaggedBy());
        assertNotNull(transaction1.getFlaggedAt());
        assertNotNull(transaction1.getUpdatedAt());
    }

}

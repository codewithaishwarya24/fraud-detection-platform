package com.fraud.transaction.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraud.transaction.api.request.CreateTransactionRequest;
import com.fraud.transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class KafkaTransactionEventListenerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaTransactionEventListener kafkaTransactionEventListener;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction_ShouldCallService() {
        // Arrange
        CreateTransactionRequest request = new CreateTransactionRequest();

        // Act
        kafkaTransactionEventListener.createTransaction(request);

        // Assert
        verify(transactionService, times(1)).createTransaction(request);
    }
}

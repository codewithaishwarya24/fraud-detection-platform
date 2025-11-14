package com.fraud.transaction.producer;

import com.fraud.common.constants.AppConstants;
import com.fraud.transaction.api.request.CreateTransactionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KafkaTransactionEventProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafkaTransactionEventProducer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction_ShouldSendMessageToKafkaTopic() {
        // Arrange
        CreateTransactionRequest request = new CreateTransactionRequest();

        // Act
        producer.createTransaction(request);

        // Assert
        verify(kafkaTemplate, times(1))
                .send(eq(AppConstants.CREATE_TRANSACTION_TOPIC), eq(request));
    }
}

package com.fraud.transaction.producer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraud.transaction.api.request.CreateTransactionRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KafkaTransactionProducerSchedulerTest {
    @Mock
    private KafkaTransactionEventProducer kafkaTransactionEventProducer;

    @Mock
    private ObjectMapper objectMapper;

    @Spy
    @InjectMocks
    private KafkaTransactionProducerScheduler kafkaTransactionProducerScheduler;

    @BeforeEach
    void setup() {
        // Avoid real threads during test
        ReflectionTestUtils.setField(kafkaTransactionProducerScheduler, "fixedThreadPoolSize", 2);
        ReflectionTestUtils.setField(kafkaTransactionProducerScheduler, "objectMapper", objectMapper);
        kafkaTransactionProducerScheduler.initExecutor();
    }

    @Test
    void testSplitTransactionsIntoBatches() {

        Mockito.when(kafkaTransactionProducerScheduler.getTransactionRequestSeed()).thenReturn(Arrays.asList(
                new CreateTransactionRequest(),
                new CreateTransactionRequest(),
                new CreateTransactionRequest()
        ));

        List<List<CreateTransactionRequest>> batches = kafkaTransactionProducerScheduler.splitTransactionsIntoBatches(2);

        Assertions.assertEquals(2, batches.size());
        Assertions.assertEquals(2, batches.get(0).size());
        Assertions.assertEquals(1, batches.get(1).size());
    }

    /*@Test
    void testProcessTransactionRequestBatch() {

        CreateTransactionRequest r1 = new CreateTransactionRequest();
        CreateTransactionRequest r2 = new CreateTransactionRequest();

        List<CreateTransactionRequest> batch = Arrays.asList(r1, r2);

        kafkaTransactionProducerScheduler.processTransactionRequestBatch(batch);

        Mockito.verify(kafkaTransactionEventProducer, Mockito.times(2))
                .createTransaction(Mockito.any(CreateTransactionRequest.class));
    }*/

    @Test
    void testProduceScheduledTransaction() {

        // Fake batch lists
        List<CreateTransactionRequest> batch1 = Arrays.asList(new CreateTransactionRequest());
        List<CreateTransactionRequest> batch2 = Arrays.asList(new CreateTransactionRequest());

        List<List<CreateTransactionRequest>> batches = Arrays.asList(batch1, batch2);

        Mockito.when(kafkaTransactionProducerScheduler.splitTransactionsIntoBatches(Mockito.anyInt()))
                .thenReturn(batches);

        ReflectionTestUtils.setField(kafkaTransactionProducerScheduler, "executor", Executors.newSingleThreadExecutor());

        kafkaTransactionProducerScheduler.produceScheduledTransaction();

        verify(kafkaTransactionEventProducer, Mockito.times(2))
                .createTransaction(any(CreateTransactionRequest.class));
    }

    @Test
    void testGetTransactionRequestSeedSuccess() throws Exception {

        Mockito.when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
                .thenReturn(Collections.emptyList());

        List<CreateTransactionRequest> result = kafkaTransactionProducerScheduler.getTransactionRequestSeed();

        Assertions.assertNotNull(result);
    }

    @Test
    void testGetTransactionRequestSeedFailure() throws Exception {

        Mockito.when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
                .thenThrow(new RuntimeException("JSON parse error"));

        List<CreateTransactionRequest> result = kafkaTransactionProducerScheduler.getTransactionRequestSeed();

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetTransactionRequestSeed_WhenJsonParseFails_ShouldReturnEmptyList() throws Exception {
        // Arrange
        //InputStream mockInputStream = TypeReference.class.getResourceAsStream("/json/seedData/transactions.json");
        // Force ObjectMapper to throw an exception
        doThrow(new RuntimeException("JSON parsing failed"))
                .when(objectMapper)
                .readValue(any(InputStream.class), any(TypeReference.class));

        // Act
        List<CreateTransactionRequest> result = kafkaTransactionProducerScheduler.getTransactionRequestSeed();

        // Assert
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testShutdownExecutor_ShouldCallShutdown() {
        // Arrange
        ExecutorService mockExecutor = mock(ExecutorService.class);
        // Inject mock executor via reflection since it's private and initialized in initExecutor()
        injectExecutor(kafkaTransactionProducerScheduler, mockExecutor);

        // Act
        kafkaTransactionProducerScheduler.shutdownExecutor();

        // Assert
        verify(mockExecutor, times(1)).shutdown();
    }

    /**
     * Utility method to inject mock ExecutorService into the private 'executor' field.
     */
    private void injectExecutor(KafkaTransactionProducerScheduler scheduler, ExecutorService executor) {
        try {
            var field = KafkaTransactionProducerScheduler.class.getDeclaredField("executor");
            field.setAccessible(true);
            field.set(scheduler, executor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

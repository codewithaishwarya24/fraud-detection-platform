package com.fraud.transaction.producer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraud.transaction.api.request.CreateTransactionRequest;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@Slf4j
public class KafkaTransactionProducerScheduler {

    private final KafkaTransactionEventProducer kafkaTransactionEventProducer;

    public KafkaTransactionProducerScheduler(KafkaTransactionEventProducer kafkaTransactionEventProducer) {
        this.kafkaTransactionEventProducer = kafkaTransactionEventProducer;
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${transaction.threadPool.size}")
    private int fixedThreadPoolSize;

    @Value("${transaction.batch.size}")
    private int batchSize;

    private ExecutorService executor;

    @PostConstruct
    public void initExecutor() {
        this.executor = Executors.newFixedThreadPool(fixedThreadPoolSize);
    }

    /**
     * Periodically reads a predefined JSON file containing a list of transaction
     * requests and publishes each transaction to Kafka. The execution interval is
     * controlled by the 'transaction.create.fixed.rate' configuration property.
     * This method is intended for automated or demo batch publishing of transactions.
     */
    /*@Scheduled(fixedRateString = "${transaction.create.fixed.rate}")
    public void produceScheduledTransaction() {
        TypeReference<List<CreateTransactionRequest>> typeReference = new TypeReference<>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/transactions.json");

        try {
            List<CreateTransactionRequest> transactionList = objectMapper.readValue(inputStream, typeReference);
            for (CreateTransactionRequest transactionRequest : transactionList) {
                kafkaTransactionEventProducer.createTransaction(transactionRequest);
            }
        } catch (Exception exception) {
            log.error("Failed to produce scheduled transactions.", exception);
        }
    }*/


    /**
     * Produces transactions on a fixed schedule by retrieving the transaction seed data,
     * splitting it into batches, and processing each batch asynchronously.
     * Each batch is handled using a CompletableFuture running on the configured executor.
     * Any batch-level failure is logged and skipped without interrupting the overall process.
     *
     * This method is triggered at a fixed rate defined by the property
     * {@code transaction.create.fixed.rate}.
     *
     * Errors that occur outside individual batch execution are captured and logged
     * to prevent the scheduler from stopping unexpectedly.
     */
    @Scheduled(fixedRateString = "${transaction.create.fixed.rate}")
    public void produceScheduledTransaction() {
        try {
            List<List<CreateTransactionRequest>> listOfTransactionRequestBatches = splitTransactionsIntoBatches(batchSize);
            List<CompletableFuture<Void>> futures = listOfTransactionRequestBatches
                    .stream().map(batch ->
                            CompletableFuture.runAsync(() -> processTransactionRequestBatch(batch), executor)
                                    .exceptionally(ex -> {
                                        log.error("Error while processing transactions batch {}", ex.getMessage());
                                        return null; // Handle exceptions gracefully for batch
                                    })
                    )
                    .toList();
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception exception) {
            log.error("Failed to produce scheduled transactions.", exception);
        }
    }

    /**
     * Processes a batch of CreateTransactionRequest objects by sending each request
     * to the Kafka transaction event producer. This method loops through the batch
     * and publishes every transaction one by one.
     *
     * @param transactionRequestBatch the list of transaction requests to process;
     *                                expected to be non-null and non-empty
     */
    private void processTransactionRequestBatch(List<CreateTransactionRequest> transactionRequestBatch) {
        for (CreateTransactionRequest transactionRequest : transactionRequestBatch) {
            kafkaTransactionEventProducer.createTransaction(transactionRequest);
        }
    }

    /**
     * Splits the full list of CreateTransactionRequest objects into smaller batches
     * of the specified size. The method retrieves the transaction seed list, checks
     * for null or empty data, and then partitions the list into sublists. Each
     * sublist represents a batch that can be processed independently.
     *
     * @param batchSize the maximum number of items allowed in each batch;
     *                  must be greater than zero
     * @return a list of batches, where each batch is a list of CreateTransactionRequest
     *         objects; returns an empty list if no transaction data is available
     */
    public List<List<CreateTransactionRequest>> splitTransactionsIntoBatches(int batchSize) {

        List<List<CreateTransactionRequest>> listOfTransactionRequestBatches = new ArrayList<>();
        List<CreateTransactionRequest> transactionList = getTransactionRequestSeed();

        if (transactionList == null || transactionList.isEmpty()) {
            return listOfTransactionRequestBatches;
        }

        for (int i = 0; i < transactionList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, transactionList.size());
            listOfTransactionRequestBatches.add(new ArrayList<>(transactionList.subList(i, end)));
        }

        return listOfTransactionRequestBatches;
    }


    /**
     * Loads a list of transaction seed data from the transactions.json file
     * located in the classpath. The JSON content is deserialized into a list
     * of CreateTransactionRequest objects. If the file is missing or the content
     * cannot be parsed, an empty list is returned and the error is logged.
     *
     * @return a list of transaction seed requests, or an empty list if loading fails
     */
    public List<CreateTransactionRequest> getTransactionRequestSeed() {
        TypeReference<List<CreateTransactionRequest>> typeReference = new TypeReference<>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/seedData/transactions.json");
        try {
            return objectMapper.readValue(inputStream, typeReference);
        } catch (Exception exception) {
            log.error("Failed to convert transactions into batches.", exception);
            return Collections.emptyList();
        }
    }

    @PreDestroy
    public void shutdownExecutor() {
        executor.shutdown();
    }
}

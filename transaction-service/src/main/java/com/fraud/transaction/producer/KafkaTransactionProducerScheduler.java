package com.fraud.transaction.producer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraud.transaction.api.request.CreateTransactionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@Slf4j
public class KafkaTransactionProducerScheduler {

    private final KafkaTransactionEventProducer kafkaTransactionEventProducer;

    public KafkaTransactionProducerScheduler(KafkaTransactionEventProducer kafkaTransactionEventProducer) {
        this.kafkaTransactionEventProducer = kafkaTransactionEventProducer;
    }

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Periodically reads a predefined JSON file containing a list of transaction
     * requests and publishes each transaction to Kafka. The execution interval is
     * controlled by the 'transaction.create.fixed.rate' configuration property.
     * This method is intended for automated or demo batch publishing of transactions.
     */
    @Scheduled(fixedRateString = "${transaction.create.fixed.rate}")
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
    }
}

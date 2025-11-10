package com.fraud.transaction.producer;

import com.fraud.common.constants.AppConstants;
import com.fraud.transaction.api.request.CreateTransactionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaTransactionEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publishes a new transaction request to the Kafka topic responsible for
     * processing transaction creation. The request object is sent as the
     * message value, and the topic name is taken from application constants.
     *
     * @param createTransactionRequest the transaction data to be produced to Kafka
     */
    public void createTransaction(CreateTransactionRequest createTransactionRequest){
        this.kafkaTemplate.send(AppConstants.CREATE_TRANSACTION_TOPIC, createTransactionRequest);
    }
}

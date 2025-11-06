package com.fraud.transaction.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraud.common.constants.AppConstants;
import com.fraud.transaction.api.request.CreateTransactionRequest;
import com.fraud.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaTransactionEventListener {

    @Autowired
    private ObjectMapper objectMapper;

    private final TransactionService transactionService;

    public KafkaTransactionEventListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * This is to consume only the latest messages
     * @param createTransactionRequest
     */
    @KafkaListener(topics = AppConstants.CREATE_TRANSACTION_TOPIC, groupId = AppConstants.TRANSACTION_GROUP_ID)
    public void createTransaction(CreateTransactionRequest createTransactionRequest) {
        transactionService.createTransaction(createTransactionRequest);
    }
}

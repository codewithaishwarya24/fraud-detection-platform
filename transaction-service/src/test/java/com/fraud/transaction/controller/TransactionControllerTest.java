package com.fraud.transaction.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraud.transaction.api.request.CreateTransactionRequest;
import com.fraud.transaction.api.request.FlagTransactionRequest;
import com.fraud.transaction.api.request.UpdateTransactionRequest;
import com.fraud.transaction.dto.TransactionDto;
import com.fraud.transaction.service.TransactionService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void healthCheck_shouldReturnStatusOK() throws Exception {
        mockMvc.perform(get("/api/transactions/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction service running OK"));
    }

    @Test
    void getTransactionById_shouldReturnTransaction() throws Exception {
        // Arrange
        String transactionId = "4ad7ec74-00e0-4d86-af31-396d587d9971";
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionId(transactionId)
                .build();

        when(transactionService.getTransactionById(transactionId)).thenReturn(transactionDto);

        // Act & Assert
        mockMvc.perform(get("/api/transactions/{id}", transactionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value(transactionId));
    }

    @Test
    void createTransaction_shouldReturnCreatedTransaction() throws Exception {
        // Arrange
        String transactionId = "tx999";

        // Load static request from JSON
        TypeReference<CreateTransactionRequest> typeReference = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/transactions.json");
        assertNotNull(inputStream, "File not found: /json/transactions.json");
        CreateTransactionRequest createTransactionRequest = objectMapper.readValue(inputStream, typeReference);

        TransactionDto createdTransactionDto = TransactionDto.builder()
                .transactionId(transactionId)
                .amount(createTransactionRequest.getAmount())
                .currency(createTransactionRequest.getCurrency())
                .merchantId(createTransactionRequest.getMerchantId())
                .cardNumberMasked(createTransactionRequest.getCardNumberMasked())
                .transactionType(createTransactionRequest.getTransactionType())
                .riskScore(createTransactionRequest.getRiskScore())
                .build();

        when(transactionService.createTransaction(any())).thenReturn(createdTransactionDto);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTransactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.endsWith("/api/transactions/" + transactionId)))
                .andExpect(jsonPath("$.transactionId").value(transactionId));
    }

    @Test
    void updateTransaction_shouldReturnUpdatedTransaction() throws Exception {
        // Arrange
        String transactionId = "tx123";

        TypeReference<UpdateTransactionRequest> typeReference = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/transactions.json");
        assertNotNull(inputStream, "File not found: /json/transactions.json");
        UpdateTransactionRequest request = objectMapper.readValue(inputStream, typeReference);

        TransactionDto transactionDto = TransactionDto.builder()
                .transactionId(transactionId)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .merchantId(request.getMerchantId())
                .cardNumberMasked(request.getCardNumberMasked())
                .transactionType(request.getTransactionType())
                .riskScore(request.getRiskScore())
                .build();

        when(transactionService.updateTransaction(eq(transactionId), any()))
                .thenReturn(transactionDto);

        mockMvc.perform(put("/api/transactions/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value(transactionId))
                .andExpect(jsonPath("$.merchantId").value(request.getMerchantId()))
                .andExpect(jsonPath("$.amount").value(request.getAmount()))
                .andExpect(jsonPath("$.currency").value(request.getCurrency()));
    }

    @Test
    void getAllTransactions_shouldReturnList() throws Exception {
        // Arrange
        List<TransactionDto> transactionsList = getTransactionDtoList();
        when(transactionService.getAllTransactions()).thenReturn(transactionsList);

        // Act & Assert
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$").isNotEmpty())
                //   .andExpect(jsonPath("$[0].transactionId").value("tx001"))
                .andExpect(jsonPath("$[0].merchantId").value("MAR101"))
                .andExpect(jsonPath("$[0].amount").value(125.25))
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[0].transactionType").value("PURCHASE"))
                .andExpect(jsonPath("$[0].riskScore").value(10));
    }

    @Test
    void testGetByMerchant_returnsTransactionList() throws Exception {
        // Given
        String merchantId = "MAR101";
        List<TransactionDto> merchantsList = getTransactionDtoList();

        when(transactionService.getTransactionsByMerchant(merchantId))
                .thenReturn(merchantsList);

        // When & Then
        mockMvc.perform(get("/api/transactions/merchant/{merchantId}", merchantId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(merchantsList.size()))
                //.andExpect(jsonPath("$[0].transactionId").value("tx001"))
                .andExpect(jsonPath("$[0].merchantId").value("MAR101"))
                .andExpect(jsonPath("$[0].amount").value(125.25));
    }

    @Test
    void testGetByMerchant_returnsEmptyList() throws Exception {
        // Given
        String merchantId = "merchant456";
        when(transactionService.getTransactionsByMerchant(merchantId))
                .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/transactions/merchant/{merchantId}", merchantId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testFlagTransaction_returnsUpdatedTransaction() throws Exception {
        // Given
        String transactionId = "txn123";
        FlagTransactionRequest request = new FlagTransactionRequest();
        request.setComment("Suspicious activity");

        TransactionDto flaggedDto = TransactionDto.builder().build();
        flaggedDto.setTransactionId(transactionId);
        //flaggedDto.setFlagged(true);
        flaggedDto.setFlagReason("Suspicious activity");

        when(transactionService.flagTransaction(eq(transactionId), any(FlagTransactionRequest.class)))
                .thenReturn(flaggedDto);

        // When & Then
        mockMvc.perform(patch("/api/transactions/txn123/flag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value("txn123"))
                // .andExpect(jsonPath("$.flagged").value(true))
                .andExpect(jsonPath("$.flagReason").value("Suspicious activity"));
    }

    @Test
    void testGetFlaggedTransactions_ReturnsList() throws Exception {

        TransactionDto tx1 = new TransactionDto();
        tx1.setTransactionId("TXN001");
        tx1.setIsFlagged(true); // ✅ fine
        tx1.setAmount(BigDecimal.valueOf(100.0));

        // Arrange
        when(transactionService.getFlaggedTransactions()).thenReturn(List.of(tx1));

        // Act & Assert
        mockMvc.perform(get("/api/transactions/flagged")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].isFlagged").value(true));
    }

    @Test
    void testGetFlaggedTransactions_EmptyList() throws Exception {
        when(transactionService.getFlaggedTransactions()).thenReturn(List.of());

        mockMvc.perform(get("/api/transactions/flagged"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    private List<TransactionDto> getTransactionDtoList() throws IOException {
        TypeReference<List<TransactionDto>> typeReference = new TypeReference<>() {};
        InputStream inputStream = getClass().getResourceAsStream("/json/transactionsList.json");
        assertNotNull(inputStream, "File not found: /json/transactionsList.json");

        return objectMapper.readValue(inputStream, typeReference);
    }

}
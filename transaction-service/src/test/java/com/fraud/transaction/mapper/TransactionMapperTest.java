package com.fraud.transaction.mapper;

import com.fraud.transaction.api.request.CreateTransactionRequest;
import com.fraud.transaction.api.request.UpdateTransactionRequest;
import com.fraud.transaction.dto.TransactionDto;
import com.fraud.transaction.entity.Transaction;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    private final TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);

    @Test
    void testToDto() {
        Transaction entity = new Transaction();
        entity.setTransactionId("TXN123");
        entity.setAmount(BigDecimal.valueOf(500.0));
        entity.setCurrency("INR");

        TransactionDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals("TXN123", dto.getTransactionId());
        assertEquals(BigDecimal.valueOf(500.0), dto.getAmount());
        assertEquals("INR", dto.getCurrency());
    }

    @Test
    void testToDtoList() {
        Transaction t1 = new Transaction();
        t1.setTransactionId("TXN1");
        t1.setAmount(BigDecimal.valueOf(100.0));

        Transaction t2 = new Transaction();
        t2.setTransactionId("TXN2");
        t2.setAmount(BigDecimal.valueOf(200.0));

        List<TransactionDto> dtoList = mapper.toDtoList(Arrays.asList(t1, t2));

        assertEquals(2, dtoList.size());
        assertEquals("TXN1", dtoList.get(0).getTransactionId());
        assertEquals("TXN2", dtoList.get(1).getTransactionId());
    }

    @Test
    void testFromCreateRequest() {
        CreateTransactionRequest request = CreateTransactionRequest.builder().build();
        request.setAmount(BigDecimal.valueOf(1000.0));
        request.setCurrency("USD");

        Transaction entity = mapper.fromCreateRequest(request);

        assertNotNull(entity);
        assertEquals(BigDecimal.valueOf(1000.0), entity.getAmount());
        assertEquals("USD", entity.getCurrency());
        assertNull(entity.getTransactionId());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
        assertNull(entity.getFlaggedAt());
        assertNull(entity.getFlaggedBy());
        assertNull(entity.getId());
    }

    @Test
    void testUpdateFromUpdateRequest() {
        Transaction entity = new Transaction();
        entity.setAmount(BigDecimal.valueOf(500.0));
        entity.setCurrency("INR");
        entity.setCreatedAt(LocalDateTime.now());

        UpdateTransactionRequest request = UpdateTransactionRequest.builder().build();
        request.setAmount(BigDecimal.valueOf(750.0)); // only this should update

        mapper.updateFromUpdateRequest(request, entity);

        assertEquals(BigDecimal.valueOf(750.0), entity.getAmount());
        assertEquals("INR", entity.getCurrency()); // unchanged
        assertNotNull(entity.getCreatedAt()); // should remain unchanged
    }
}
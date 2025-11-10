package com.fraud.transaction.mapper;

import com.fraud.transaction.api.request.CreateTransactionRequest;
import com.fraud.transaction.api.request.UpdateTransactionRequest;
import com.fraud.transaction.dto.TransactionDto;
import com.fraud.transaction.entity.Transaction;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {

    // Entity -> DTO
    TransactionDto toDto(Transaction entity);

    List<TransactionDto> toDtoList(List<Transaction> entities);

    // CreateRequest -> Entity
    // transactionId, createdAt, updatedAt, flaggedAt, flaggedBy should not be supplied by client;
    // they will be set by the service / DB, so ignore them here (no overwrite).
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "transactionId", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "flaggedAt", ignore = true),
            @Mapping(target = "flaggedBy", ignore = true),
            @Mapping(target = "isFlagged", ignore = true) // service will default this if needed
    })
    Transaction fromCreateRequest(CreateTransactionRequest request);

    // UpdateRequest -> Entity (in-place). Ignore nulls (configured at mapper level). Also avoid overwriting audit fields.
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "transactionId", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "flaggedAt", ignore = true),
            @Mapping(target = "flaggedBy", ignore = true)
    })
    void updateFromUpdateRequest(UpdateTransactionRequest request, @MappingTarget Transaction entity);
}

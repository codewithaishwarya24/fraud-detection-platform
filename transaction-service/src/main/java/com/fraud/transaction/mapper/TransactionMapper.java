package com.fraud.transaction.mapper;

import com.fraud.transaction.entity.Transaction;
import com.fraud.transaction.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

/**
 * Mapper to convert between {@link Transaction} entities and {@link TransactionDto} DTOs.
 *
 * MapStruct will generate the implementation; this interface is detected by Spring
 * because componentModel = "spring".
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface TransactionMapper {

    /**
     * Map an entity to a DTO.
     *
     * @param transaction entity to map
     * @return mapped DTO (or default DTO if {@code transaction} is null)
     */
    TransactionDto toDto(Transaction transaction);

    /**
     * Map a DTO to an entity.
     *
     * @param transactionDto dto to map
     * @return mapped entity (or default entity if {@code transactionDto} is null)
     */
    Transaction toEntity(TransactionDto transactionDto);

    /**
     * Map a list of entities to a list of DTOs.
     * MapStruct automatically handles collection mapping; this is here for clarity and explicit usage.
     *
     * @param transactions list of entities
     * @return list of DTOs (empty list if input is null)
     */
    List<TransactionDto> toDtoList(List<Transaction> transactions);

    /**
     * Map a list of DTOs to a list of entities.
     *
     * @param dtos list of DTOs
     * @return list of entities (empty list if input is null)
     */
    List<Transaction> toEntityList(List<TransactionDto> dtos);
}

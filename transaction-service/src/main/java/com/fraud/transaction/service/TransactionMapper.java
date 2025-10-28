package com.fraud.transaction.service;

import com.fraud.transaction.entity.Transaction;
import com.fraud.transaction.model.TransactionDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDto mapTransactionToTransactionDto(Transaction transaction);

    Transaction mapTransactionDtoToTransaction(TransactionDto transactionDto);

    List<TransactionDto> toTransactionDtoList(List<Transaction> transactionList);
}

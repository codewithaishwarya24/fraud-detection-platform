package com.fraud.transaction.service;

import com.fraud.transaction.entity.Transaction;
import com.fraud.transaction.model.TransactionDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-28T23:33:03+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.16 (Eclipse Adoptium)"
)
@Component
public class TransactionMapperImpl implements TransactionMapper {

    @Override
    public TransactionDto mapTransactionToTransactionDto(Transaction transaction) {
        if ( transaction == null ) {
            return null;
        }

        TransactionDto transactionDto = new TransactionDto();

        transactionDto.setTransactionId( transaction.getTransactionId() );
        transactionDto.setAmount( transaction.getAmount() );
        transactionDto.setCurrency( transaction.getCurrency() );
        transactionDto.setMerchantId( transaction.getMerchantId() );
        transactionDto.setCardNumberMasked( transaction.getCardNumberMasked() );
        transactionDto.setCardType( transaction.getCardType() );
        transactionDto.setTransactionType( transaction.getTransactionType() );
        transactionDto.setResponseCode( transaction.getResponseCode() );
        transactionDto.setIsFlagged( transaction.getIsFlagged() );
        transactionDto.setFlagReason( transaction.getFlagReason() );
        transactionDto.setRiskScore( transaction.getRiskScore() );
        transactionDto.setReviewStatus( transaction.getReviewStatus() );
        transactionDto.setChannel( transaction.getChannel() );
        transactionDto.setIpAddress( transaction.getIpAddress() );
        transactionDto.setDeviceId( transaction.getDeviceId() );
        transactionDto.setLocation( transaction.getLocation() );
        transactionDto.setTransactionTime( transaction.getTransactionTime() );
        transactionDto.setCreatedAt( transaction.getCreatedAt() );

        return transactionDto;
    }

    @Override
    public Transaction mapTransactionDtoToTransaction(TransactionDto transactionDto) {
        if ( transactionDto == null ) {
            return null;
        }

        Transaction.TransactionBuilder transaction = Transaction.builder();

        transaction.transactionId( transactionDto.getTransactionId() );
        transaction.amount( transactionDto.getAmount() );
        transaction.currency( transactionDto.getCurrency() );
        transaction.merchantId( transactionDto.getMerchantId() );
        transaction.cardNumberMasked( transactionDto.getCardNumberMasked() );
        transaction.cardType( transactionDto.getCardType() );
        transaction.transactionType( transactionDto.getTransactionType() );
        transaction.responseCode( transactionDto.getResponseCode() );
        transaction.isFlagged( transactionDto.getIsFlagged() );
        transaction.flagReason( transactionDto.getFlagReason() );
        transaction.riskScore( transactionDto.getRiskScore() );
        transaction.reviewStatus( transactionDto.getReviewStatus() );
        transaction.channel( transactionDto.getChannel() );
        transaction.ipAddress( transactionDto.getIpAddress() );
        transaction.deviceId( transactionDto.getDeviceId() );
        transaction.location( transactionDto.getLocation() );
        transaction.transactionTime( transactionDto.getTransactionTime() );
        transaction.createdAt( transactionDto.getCreatedAt() );

        return transaction.build();
    }

    @Override
    public List<TransactionDto> toTransactionDtoList(List<Transaction> transactionList) {
        if ( transactionList == null ) {
            return null;
        }

        List<TransactionDto> list = new ArrayList<TransactionDto>( transactionList.size() );
        for ( Transaction transaction : transactionList ) {
            list.add( mapTransactionToTransactionDto( transaction ) );
        }

        return list;
    }
}

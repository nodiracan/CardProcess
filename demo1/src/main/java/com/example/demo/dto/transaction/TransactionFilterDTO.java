package com.example.demo.dto.transaction;

import com.example.demo.entity.Transaction;
import com.example.demo.enums.TransactionType;

public record TransactionFilterDTO(TransactionType type, String transactionId, String externalId , String currency,int page,  int size ) {
    public TransactionFilterDTO {
        if (size <= 0) {
            size = 10;
        }
    }
}

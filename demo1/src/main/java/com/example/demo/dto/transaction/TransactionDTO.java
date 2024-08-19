package com.example.demo.dto.transaction;

import com.example.demo.enums.Currency;
import com.example.demo.enums.TransactionPurpose;

public record TransactionDTO(String transactionId,
                             String externalId,
                             String cardId,
                             Long amount,
                             Long afterBalance,
                             Currency currency,
                             TransactionPurpose purpose,
                             Long exchangeRate) {
}

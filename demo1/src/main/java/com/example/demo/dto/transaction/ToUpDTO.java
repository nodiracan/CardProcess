package com.example.demo.dto.transaction;

import com.example.demo.enums.Currency;

public record ToUpDTO(String externalId, long amount, Currency currency) {
}

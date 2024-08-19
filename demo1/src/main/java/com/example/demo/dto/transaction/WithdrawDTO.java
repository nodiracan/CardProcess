package com.example.demo.dto.transaction;

import com.example.demo.enums.Currency;
import com.example.demo.enums.TransactionPurpose;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

public record WithdrawDTO(@NotNull String externalId,

                          @NotNull Long amount,

                          Currency currency,

                          @NotNull TransactionPurpose purpose) {
}

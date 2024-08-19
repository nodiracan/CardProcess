package com.example.demo.dto.card;

import com.example.demo.enums.CardStatus;
import com.example.demo.enums.Currency;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
public record CardDTO(String cardId,
                      @NotBlank @Nonnull Long userid,
                      @NotBlank @Nonnull CardStatus status,
                      Currency currency,
                      Long balance) {
}

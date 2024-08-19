package com.example.demo.dto.card;
import com.example.demo.enums.CardStatus;
import com.example.demo.enums.Currency;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
public record CardCreateDTO(@Nonnull @NotBlank Long user_id,
                            @Nonnull @NotBlank CardStatus status,
                            Long InitialAmount, Currency currency)  {
}

package com.example.demo.entity;

import com.example.demo.enums.CardStatus;
import com.example.demo.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Enumerated(EnumType.STRING)
    private CardStatus status  = CardStatus.ACTIVE;

    @Max(value = 10000)
    private Long initialAmount = 0L;

    @Enumerated(EnumType.STRING)
    private Currency currency = Currency.UZS;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AuthUser user;

   @OneToOne
    @JoinColumn(name = "idempotency_key", referencedColumnName = "key")
    private IdempotencyKey idempotencyKey;
}


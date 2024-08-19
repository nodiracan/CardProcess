package com.example.demo.entity;

import com.example.demo.enums.CardStatus;
import com.example.demo.enums.Currency;
import com.example.demo.enums.TransactionPurpose;
import com.example.demo.enums.TransactionType;
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
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String externalId;

    @Enumerated(EnumType.STRING)
    private CardStatus status  = CardStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    private Currency currency  = Currency.UZS;

    private Long amount;
    private Long afterBalance = 0L;

    @Enumerated(EnumType.STRING)
    private TransactionPurpose purpose;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

   @OneToOne
    @JoinColumn(name = "idempotency_key", referencedColumnName = "key")
    private IdempotencyKey idempotencyKey;
    private Long exchangeRate;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

}


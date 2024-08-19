package com.example.demo.repository;

import com.example.demo.entity.Card;
import com.example.demo.entity.IdempotencyKey;
import com.example.demo.entity.Transaction;
import com.example.demo.enums.CardStatus;
import com.example.demo.enums.TransactionType;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, BaseRepository {
    Optional<Transaction> findByIdempotencyKey(IdempotencyKey key);

    List<Transaction> findByCardAndType(Card card, TransactionType type);
    long countTransactionsByCardAndIdAndExternalId(Card card, String transactionId, String externalId);
}

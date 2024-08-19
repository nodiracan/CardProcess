package com.example.demo.repository;

import com.example.demo.entity.Card;
import com.example.demo.enums.CardStatus;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String>, BaseRepository {
    List<Card> findByUserIdAndStatusIsNot(Long userId, CardStatus status);

    Optional<Card> findByIdAndStatusIsNot(String cardId, CardStatus status);
}

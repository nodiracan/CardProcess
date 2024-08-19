package com.example.demo.service;

import com.example.demo.dto.card.CardCreateDTO;
import com.example.demo.dto.card.CardDTO;
import com.example.demo.dto.transaction.*;
import com.example.demo.enums.TransactionType;
import com.example.demo.service.base.BaseService;

public interface CardService extends BaseService {
    CardDTO createCard(String idKey, CardCreateDTO dto);

    CardDTO getCard(String cardId);

    String blockCard(String cardId, String eTag);

    String unblockCard(String cardId, String eTag);

    TransactionDTO debitCard(String cardId, WithdrawDTO request, String idempotencyKey);

    TransactionDTO credit(String cardId, ToUpDTO request, String idempotencyKey);

    TransactionHistoryDTO getTransactions(String cardId, TransactionType type, int page, TransactionFilterDTO dto);
}

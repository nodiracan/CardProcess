package com.example.demo.service.impl;

import com.example.demo.constant.StaticMessages;
import com.example.demo.dto.card.CardCreateDTO;
import com.example.demo.dto.card.CardDTO;
import com.example.demo.dto.transaction.TransactionDTO;
import com.example.demo.dto.transaction.WithdrawDTO;
import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Card;
import com.example.demo.entity.IdempotencyKey;
import com.example.demo.entity.Transaction;
import com.example.demo.enums.CardStatus;
import com.example.demo.enums.Currency;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.CardMapper;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.IdempotencyKeyRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.CardService;
import com.example.demo.service.base.AbstractService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class ExchangeRateService  {

    public Long getExchangeRate(Currency fromCurrency, Currency toCurrency) {
        if (fromCurrency == Currency.USD && toCurrency == Currency.UZS) {
            return 12000L;
        } else if (fromCurrency == Currency.UZS && toCurrency == Currency.USD) {
            return 1L / 12000L;
        }
        return 1L;
    }
}

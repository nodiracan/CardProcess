package com.example.demo.service.impl;

import com.example.demo.constant.StaticMessages;
import com.example.demo.dto.card.CardCreateDTO;
import com.example.demo.dto.card.CardDTO;
import com.example.demo.dto.transaction.*;
import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Card;
import com.example.demo.entity.IdempotencyKey;
import com.example.demo.entity.Transaction;
import com.example.demo.enums.CardStatus;
import com.example.demo.enums.Currency;
import com.example.demo.enums.TransactionType;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.mapper.CardMapper;
import com.example.demo.mapper.TransactionMapper;
import com.example.demo.repository.AuthUserRepository;
import com.example.demo.repository.CardRepository;
import com.example.demo.repository.IdempotencyKeyRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.CardService;
import com.example.demo.service.base.AbstractService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class CardServiceImpl extends AbstractService<CardRepository, CardMapper> implements CardService {


    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private final CardRepository cardRepository;
    private final AuthUserRepository authUserRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final ExchangeRateService exchangeRateService;
    private final CurrencyConvertService currencyConvertService;

    public CardServiceImpl(CardRepository repository, CardMapper mapper, IdempotencyKeyRepository idempotencyKeyRepository,
                           CardRepository cardRepository,
                           AuthUserRepository authUserRepository, TransactionRepository transactionRepository, TransactionMapper transactionMapper, ExchangeRateService exchangeRateService, CurrencyConvertService currencyConvertService) {
        super(repository, mapper);
        this.idempotencyKeyRepository = idempotencyKeyRepository;
        this.cardRepository = cardRepository;
        this.authUserRepository = authUserRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.exchangeRateService = exchangeRateService;
        this.currencyConvertService = currencyConvertService;
    }

    @Override
    @Transactional
    public CardDTO createCard(String idempotencyKey, CardCreateDTO dto) {
        Optional<IdempotencyKey> key = idempotencyKeyRepository.findByKey(idempotencyKey);

        if (key.isPresent()) {
            IdempotencyKey existKey = key.get();
            if (existKey.getCreatedAt().plusHours(24).isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Idempotency Key has expired");
            }
            Card card1 = cardRepository.findByUserIdAndStatusIsNot(dto.user_id(), CardStatus.CLOSED)
                    .stream()
                    .filter(card -> card.getIdempotencyKey().getKey().equals(existKey.getKey()))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException("Idempotency key already exist, but card not found"));
            return mapper.toDTO(card1);

        }

        if (cardRepository.findByUserIdAndStatusIsNot(dto.user_id(), CardStatus.CLOSED).size() >= 3) {
            throw new BadRequestException("User has already 3 active cards");
        }

        IdempotencyKey newKey = new IdempotencyKey();
        newKey.setKey(idempotencyKey);
        newKey.setCreatedAt(LocalDateTime.now());
        newKey.setResponse("Response key concerns to: " + dto.user_id());
        idempotencyKeyRepository.save(newKey);
        AuthUser user = authUserRepository.findById(dto.user_id())
                .orElseThrow(() -> new NotFoundException(StaticMessages.USER_NOT_FOUND + dto.user_id()));

        Card card = new Card();
        card.setUser(user);
        card.setCurrency(Optional.ofNullable(dto.currency()).orElse(Currency.UZS));
        card.setStatus(Optional.ofNullable(dto.status()).orElse(CardStatus.ACTIVE));
        card.setInitialAmount(Optional.ofNullable(dto.InitialAmount()).orElse(0L));
        card.setIdempotencyKey(newKey);
        cardRepository.save(card);

        return mapper.toDTO(card);
    }

    @Override
    public CardDTO getCard(String cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(StaticMessages.CARD_NOT_FOUND));
        String authenticatedUsername = AuthServiceImpl.getAuthenticatedUsername();
        if (!authenticatedUsername.equals(card.getUser().getUsername())) {
            throw new UnauthorizedException("Please first Authorize to get your Card");
        } else return mapper.toDTO(card);
    }

    @Override
    @Transactional
    public String blockCard(String cardId, String eTag) {
        Card card = extracted(cardId, eTag);

        if (!card.getStatus().equals(CardStatus.ACTIVE)) {
            throw new BadRequestException(("Card is not ACTIVE. You can block Only Active cards!"));
        }

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
        return StaticMessages.BLOCK_CARD;

    }

    @Override
    @Transactional
    public String unblockCard(String cardId, String eTag) {
        Card card = extracted(cardId, eTag);
        if (!card.getStatus().equals(CardStatus.BLOCKED)) {
            throw new BadRequestException(("Card is not Blocked. You can block Only BLocked cards!"));
        }

        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
        return StaticMessages.UNBLOCK_CARD;
    }

    @Override
    @Transactional
    public TransactionDTO debitCard(String cardId, WithdrawDTO dto, String idempotencyKey) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(StaticMessages.CARD_NOT_FOUND));
        String authenticatedUsername = AuthServiceImpl.getAuthenticatedUsername();
        if (!authenticatedUsername.equals(card.getUser().getUsername())) {
            throw new UnauthorizedException("Please first Authorize to do action on your Card");
        }

        IdempotencyKey byKey = idempotencyKeyRepository.findByKey(idempotencyKey).orElseThrow(
                ()-> new NotFoundException(StaticMessages.KEY_NOT_FOUND));

        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(byKey);
        if (existingTransaction.isPresent()) {
            return transactionMapper.toDto(existingTransaction.get());
        }

        if (!card.getStatus().equals(CardStatus.ACTIVE)) {
            throw new BadRequestException(("Card is not ACTIVE. You can block Only Active cards!"));
        }

        Long withDrawAmount = dto.amount();
        Long exchangeRate = null;

        if (dto.currency() != card.getCurrency()){
            exchangeRate = exchangeRateService.getExchangeRate(dto.currency(), card.getCurrency());
            withDrawAmount = currencyConvertService.convertCurrency(dto.amount(), exchangeRate);
        }

        if (card.getInitialAmount() < withDrawAmount){
            throw new BadRequestException(StaticMessages.INSUFFICIENT_FUNDS);
        }

        card.setInitialAmount(card.getInitialAmount() - withDrawAmount);
        cardRepository.save(card);


        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setExternalId(dto.externalId());
        transaction.setAmount(dto.amount());
        transaction.setAfterBalance(card.getInitialAmount());
        transaction.setCurrency(dto.currency());
        transaction.setPurpose(dto.purpose());
        transaction.setExchangeRate(exchangeRate);
        transaction.setIdempotencyKey(idempotencyKeyRepository.findByKey(idempotencyKey)
                .orElseThrow(() -> new NotFoundException(StaticMessages.KEY_NOT_FOUND)));
        transaction.setType(TransactionType.DEBIT);
        transactionRepository.save(transaction);

        return transactionMapper.toDto(transaction);

    }

    @Override
    @Transactional
    public TransactionDTO credit(String cardId, ToUpDTO dto, String idempotencyKey) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(StaticMessages.CARD_NOT_FOUND));
        String authenticatedUsername = AuthServiceImpl.getAuthenticatedUsername();
        if (!authenticatedUsername.equals(card.getUser().getUsername())) {
            throw new UnauthorizedException("Please first Authorize to do action on your Card");
        }

        IdempotencyKey byKey = idempotencyKeyRepository.findByKey(idempotencyKey).orElseThrow(
                ()-> new NotFoundException(StaticMessages.KEY_NOT_FOUND));

        Optional<Transaction> existingTransaction = transactionRepository.findByIdempotencyKey(byKey);
        if (existingTransaction.isPresent()) {
            return transactionMapper.toDto(existingTransaction.get());
        }

        if (!card.getStatus().equals(CardStatus.ACTIVE)) {
            throw new BadRequestException(("Card is not ACTIVE. You can block Only Active cards!"));
        }
        long topUpAmount = dto.amount();
        Long exchangeRate = null;
        if (dto.currency() != card.getCurrency()) {
            exchangeRate = exchangeRateService.getExchangeRate(dto.currency(), card.getCurrency());
            topUpAmount = currencyConvertService.convertCurrency(dto.amount(), exchangeRate);
        }

        card.setInitialAmount(card.getInitialAmount()+ topUpAmount);
        Card saved = cardRepository.save(card);

        Transaction transaction = new Transaction();
        transaction.setCard(card);
        transaction.setExternalId(dto.externalId());
        transaction.setAmount(dto.amount());
        transaction.setAfterBalance(saved.getInitialAmount());
        transaction.setCurrency(dto.currency());
        transaction.setExchangeRate(exchangeRate);
        transaction.setIdempotencyKey(byKey);
        transaction.setType(TransactionType.CREDIT);

        Transaction save = transactionRepository.save(transaction);
        return transactionMapper.toDto(save);

    }

    @Override
    public TransactionHistoryDTO getTransactions(String cardId, TransactionType type, int page, TransactionFilterDTO dto) {
        Pageable pageable = (Pageable) PageRequest.of(page, dto.size());

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(StaticMessages.CARD_NOT_FOUND));

        List<Transaction> transactions = transactionRepository.findByCardAndType(card, type);
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(this::convertToDTO)
                .toList();

        TransactionHistoryDTO response = new TransactionHistoryDTO(
                page, dto.size(), transactions.size(),(int) Math.ceil((double)transactions.size()/dto.size()),
                transactionDTOs
        );

        return response;
    }

    private TransactionDTO convertToDTO(Transaction transaction) {

        if (transaction == null) {
            return null;
        }
       return transactionMapper.toDto(transaction);
    }

    private Card extracted(String cardId, String eTag) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException(StaticMessages.CARD_NOT_FOUND));

        String authenticatedUsername = AuthServiceImpl.getAuthenticatedUsername();
        if (!authenticatedUsername.equals(card.getUser().getUsername())) {
            throw new UnauthorizedException("Please first Authorize to do action on your Card");
        }
        String cardKey = card.getIdempotencyKey().getKey();
        if (!eTag.equals(cardKey)) {
            throw new BadRequestException("Card eTag data doesn't match");
        }

        return card;
    }
}

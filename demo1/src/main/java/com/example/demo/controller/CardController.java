package com.example.demo.controller;

import com.example.demo.controller.base.AbstractController;
import com.example.demo.dto.card.CardCreateDTO;
import com.example.demo.dto.card.CardDTO;
import com.example.demo.dto.transaction.*;
import com.example.demo.entity.Transaction;
import com.example.demo.enums.TransactionType;
import com.example.demo.service.impl.CardServiceImpl;
import static com.example.demo.controller.base.AbstractController.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Card")
@Slf4j
@RestController
@RequestMapping(PATH+"/cards")
public class CardController extends AbstractController<CardServiceImpl> {

    public CardController(CardServiceImpl service) {
        super(service);
    }


    @PostMapping(value = "/create")
    public ResponseEntity<CardDTO> createCard(@RequestHeader("Idempotency-Key") String idempotencyKey, @RequestBody CardCreateDTO dto){
        log.info("REST: [POST] create() method  Card : {}  ", idempotencyKey);
        CardDTO card = service.createCard(idempotencyKey, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }

    @GetMapping(value = "/{cardId}")
    public ResponseEntity<CardDTO> getCard(@PathVariable(name = "cardId") String cardId){
        log.info("REST: [GET] getCard() method  Card : {}  ", cardId);
        CardDTO card = service.getCard(cardId);
        return ResponseEntity.ok(card);
    }

    @PutMapping(value = "/{cardId}/block")
    public ResponseEntity<String> blockCard(@PathVariable(name = "cardId") String cardId, @RequestHeader("If-Match") String eTag){
        log.info("REST: [POST] blockCard() method  Card : {}  ", cardId);
        String card = service.blockCard(cardId, eTag);
        return ResponseEntity.ok(card);
    }

    @PutMapping(value = "/{cardId}/unblock")
    public ResponseEntity<String> unblockCard(@PathVariable(name = "cardId") String cardId, @RequestHeader("If-Match") String eTag){
        log.info("REST: [POST] unblockCard() method  Card : {}  ", cardId);
        String card = service.unblockCard(cardId, eTag);
        return ResponseEntity.ok(card);
    }

    @PostMapping(value = "/{cardId}/debit")
    public ResponseEntity<TransactionDTO> debitCard(@PathVariable(name = "cardId") String cardId, @RequestBody WithdrawDTO request, @RequestHeader("Idempotency-Key") String idempotencyKey){
        log.info("REST: [POST] debitCard() method  Card : {}  ", cardId);
        TransactionDTO dto =  service.debitCard(cardId, request, idempotencyKey);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/{cardId}/credit")
    public ResponseEntity<TransactionDTO> credit(@PathVariable(name = "cardId") String cardId,
                                          @RequestBody ToUpDTO request,
                                          @RequestHeader("Idempotency-Key") String idempotencyKey){
        log.info("REST: [POST] credit() method  Card : {}  ", cardId);
        TransactionDTO dto =  service.credit(cardId, request, idempotencyKey);
        return ResponseEntity.ok(dto);
    }


    @GetMapping(value = "/{cardId}/transactions")
    public ResponseEntity<TransactionHistoryDTO> getCards(@PathVariable(name = "cardId") String cardId,
                                                         @RequestParam(value = "type", required = false) TransactionType type,
                                                         @RequestParam(value = "page", defaultValue = "1") int page,
                                                         @RequestBody TransactionFilterDTO dto){
        log.info("REST: [GET] getCards() method  Card : {}  ", cardId);
        TransactionHistoryDTO transactions = service.getTransactions(cardId, type, page, dto);
        return ResponseEntity.ok(transactions);
    }

}

package com.a2sv.bankdashboard.controller;

import com.a2sv.bankdashboard.dto.request.CardRequest;
import com.a2sv.bankdashboard.dto.response.CardResponse;
import com.a2sv.bankdashboard.dto.response.CardResponseDetailed;
import com.a2sv.bankdashboard.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<CardResponseDetailed> addCard(@RequestBody CardRequest cardRequest) {
        CardResponseDetailed newCard = cardService.addCard(cardRequest);
        return ResponseEntity.ok(newCard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCard(@PathVariable Long id) {
        cardService.removeCard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDetailed> getCardById(@PathVariable Long id) {
        CardResponseDetailed card = cardService.getCardById(id);
        return ResponseEntity.ok(card);
    }

    @GetMapping
    public ResponseEntity<List<CardResponse>> getAllCards() {
        List<CardResponse> cards = cardService.getAllCardsByUserId();
        return ResponseEntity.ok(cards);
    }
}
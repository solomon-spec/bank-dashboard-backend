package com.a2sv.bankdashboard.controller;

import com.a2sv.bankdashboard.dto.request.CardRequest;
import com.a2sv.bankdashboard.dto.response.CardResponse;
import com.a2sv.bankdashboard.dto.response.CardResponseDetailed;
import com.a2sv.bankdashboard.dto.response.PagedResponse;
import com.a2sv.bankdashboard.service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<CardResponseDetailed> addCard(@Valid @RequestBody CardRequest cardRequest) {
        CardResponseDetailed newCard = cardService.addCard(cardRequest);
        return ResponseEntity.ok(newCard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCard(@PathVariable String id) {
        cardService.removeCard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDetailed> getCardById(@PathVariable String id) {
        CardResponseDetailed card = cardService.getCardById(id);
        return ResponseEntity.ok(card);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<CardResponse>> getAllCards(@RequestParam int page, @RequestParam int size) {
        PagedResponse<CardResponse> cards = cardService.getAllCardsByUserId(page, size);
        return ResponseEntity.ok(cards);
    }
}
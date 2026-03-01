package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @GetMapping
    public ResponseEntity<List<CardDto>> getCards() {
        return ResponseEntity.ok(cardService.getAllCards());
    }

    @PostMapping("/createCard")
    public ResponseEntity<CardDto> createCard(@RequestParam Long userId) {
        return ResponseEntity.ok(cardService.createCard(userId));
    }

    @PutMapping("/{cardNumber}/status")
    public ResponseEntity<CardDto> updateStatus(@PathVariable String cardNumber, @RequestParam CardStatus status) {
        return ResponseEntity.ok(cardService.changeStatus(cardNumber, status));
    }

    @DeleteMapping("/{cardNumber}/delete")
    public ResponseEntity<Void> deleteCard(@PathVariable String cardNumber) {
        cardService.deleteCard(cardNumber);
        return ResponseEntity.ok().build();
    }
}

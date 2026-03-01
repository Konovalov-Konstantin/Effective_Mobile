package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.entity.CardStatus;

import java.util.List;

public interface CardService {
    List<CardDto> getAllCards();
    CardDto createCard(Long userId);
    void transfer(TransferRequest request);
    CardDto changeStatus(String cardNumber, CardStatus newStatus);
    void deleteCard(String cardNumber);
}

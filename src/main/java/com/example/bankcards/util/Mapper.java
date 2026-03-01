package com.example.bankcards.util;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;

public class Mapper {

    public static CardDto toCardDto(Card card) {
        return CardDto.builder()
                .id(card.getId())
                .cardNumber(Util.getMaskerCardNumber(card.getCardNumber()))
                .balance(card.getBalance())
                .status(card.getStatus().name())
                .expireDate(card.getExpireDate())
                .ownerId(card.getOwner().getId())
                .ownerName(card.getOwner().getUsername())
                .build();
    }
}

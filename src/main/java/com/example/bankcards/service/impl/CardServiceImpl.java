package com.example.bankcards.service.impl;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AccessDeniedException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.Mapper;
import com.example.bankcards.util.Util;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.bankcards.entity.CardStatus.ACTIVE;
import static com.example.bankcards.entity.Role.ADMIN;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Override
    public List<CardDto> getAllCards() {
        User currentUser = getCurrentUser();
        List<Card> cards;

        if (isAdmin(currentUser)) {
            cards = cardRepository.findAll();
        } else {
            cards = cardRepository.findByOwnerId(currentUser.getId());
        }
        return cards.stream().map(Mapper::toCardDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CardDto createCard(Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Клиент %d не найден", userId)));

        Card card = Card.builder()
                .cardNumber(Util.generateCardNumber())
                .expireDate(Date.from(ZonedDateTime.now().plusYears(1).toInstant()))
                .balance(BigDecimal.ZERO)
                .owner(owner)
                .status(ACTIVE)
                .build();
        return Mapper.toCardDto(cardRepository.save(card));
    }

    @Override
    @Transactional
    public void transfer(TransferRequest request) {
        User currentUser = getCurrentUser();

        Card fromCard = cardRepository.findByCardNumber(request.getFromCardNumber())
                .orElseThrow(() -> new CardNotFoundException(String.format("Карта %s не найдена",
                        Util.getMaskerCardNumber(request.getFromCardNumber()))));

        Card toCard = cardRepository.findByCardNumber(request.getToCardNumber())
                .orElseThrow(() -> new CardNotFoundException(String.format("Карта %s не найдена",
                        Util.getMaskerCardNumber(request.getToCardNumber()))));

        if (!fromCard.getOwner().getId().equals(toCard.getOwner().getId())) {
            throw new AccessDeniedException("Переводы разрешены только между своими картами");
        }
        if (!fromCard.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Клиент предъявил чужую карту");
        }
        if (fromCard.getStatus() != ACTIVE || toCard.getStatus() != ACTIVE) {
            throw new IllegalStateException("Карты должны быть активными");
        }
        if (fromCard.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException(String.format("На карте %s недостаточно средств",
                    Util.getMaskerCardNumber(fromCard.getCardNumber())));
        }

        fromCard.setBalance(fromCard.getBalance().subtract(request.getAmount()));
        toCard.setBalance(toCard.getBalance().add(request.getAmount()));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);
    }

    @Override
    @Transactional
    public CardDto changeStatus(String cardNumber, CardStatus newStatus) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Карта %s не найдена",
                        Util.getMaskerCardNumber(cardNumber))));
        card.setStatus(newStatus);
        return Mapper.toCardDto(cardRepository.save(card));
    }

    @Override
    @Transactional
    public void deleteCard(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Карта %s не найдена",
                        Util.getMaskerCardNumber(cardNumber))));
        cardRepository.delete(card);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Клиент %s не найден", username)));
    }

    private boolean isAdmin(User currentUser) {
        return currentUser.getRole().name().equals(ADMIN.name());
    }
}

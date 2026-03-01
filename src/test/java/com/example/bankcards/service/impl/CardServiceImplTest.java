package com.example.bankcards.service.impl;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CardServiceImpl cardService;

    private User admin;
    private User user;
    private Card card1;
    private Card card2;

    @BeforeEach
    void setUp() {
        admin = new User();
        admin.setId(1L);
        admin.setUsername("admin");
        admin.setRole(Role.ADMIN);

        user = new User();
        user.setId(2L);
        user.setUsername("user");
        user.setRole(Role.USER);

        card1 = new Card();
        card1.setId(1L);
        card1.setCardNumber("4000-1111");
        card1.setBalance(new BigDecimal("1000.00"));
        card1.setStatus(CardStatus.ACTIVE);
        card1.setOwner(user);

        card2 = new Card();
        card2.setId(2L);
        card2.setCardNumber("4000-2222");
        card2.setBalance(new BigDecimal("500.00"));
        card2.setStatus(CardStatus.ACTIVE);
        card2.setOwner(user);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void shouldReturnAllCardsAsAdmin() {
        when(authentication.getName()).thenReturn("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(cardRepository.findAll()).thenReturn(List.of(card1, card2));

        List<CardDto> result = cardService.getAllCards();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnOnlyUserCards() {
        when(authentication.getName()).thenReturn("user");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(cardRepository.findByOwnerId(2L)).thenReturn(List.of(card1, card2));

        List<CardDto> result = cardService.getAllCards();

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
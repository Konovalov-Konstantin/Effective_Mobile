package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.security.JwtAuthenticationFilter;
import com.example.bankcards.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.example.bankcards.entity.CardStatus.ACTIVE;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardController.class)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CardService cardService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private List<CardDto> cardList;

    CardDto cardDto1;
    CardDto cardDto2;

    @BeforeEach
    void setUp() {
        cardDto1 = CardDto.builder()
                .id(1L)
                .cardNumber("7264 6261 5756 4895")
                .balance(new BigDecimal("1000.00"))
                .status(ACTIVE.name())
                .ownerId(1L)
                .build();
        cardDto2 = CardDto.builder()
                .id(2L)
                .cardNumber("7264 6261 5756 4896")
                .balance(new BigDecimal("2000.00"))
                .status(ACTIVE.name())
                .ownerId(2L)
                .build();

        cardList = Arrays.asList(cardDto1, cardDto2);
    }

    @Test
    @WithMockUser
    void shouldReturn200_Status() throws Exception {
        when(cardService.getAllCards()).thenReturn(cardList);

        mockMvc.perform(get("/api/cards").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn401_Status() throws Exception {
        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void shouldCreateNewCardWithAdminRole() throws Exception {
        when(cardService.createCard(2L)).thenReturn(cardDto2);

        mockMvc.perform(post("/api/cards/createCard")
                        .param("userId", "2")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
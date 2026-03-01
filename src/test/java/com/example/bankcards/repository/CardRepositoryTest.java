package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class CardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private Card card1;
    private Card card2;
    private Card card3;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setUsername("test_user1");
        user1.setPassword("password1");
        user1.setRole(Role.USER);
        user1 = entityManager.persistAndFlush(user1);

        user2 = new User();
        user2.setUsername("test_user2");
        user2.setPassword("password2");
        user2.setRole(Role.USER);
        user2 = entityManager.persistAndFlush(user2);

        card1 = Card.builder()
                .cardNumber("4000-1111-1111-1111")
                .balance(new BigDecimal("1000.00"))
                .status(CardStatus.ACTIVE)
                .owner(user1)
                .build();
        card1 = entityManager.persistAndFlush(card1);

        card2 = Card.builder()
                .cardNumber("4000-2222-2222-2222")
                .balance(new BigDecimal("500.00"))
                .status(CardStatus.ACTIVE)
                .owner(user1)
                .build();
        card2 = entityManager.persistAndFlush(card2);

        card3 = Card.builder()
                .cardNumber("4000-3333-3333-3333")
                .balance(new BigDecimal("750.00"))
                .status(CardStatus.BLOCKED)
                .owner(user2)
                .build();
        card3 = entityManager.persistAndFlush(card3);
    }

    @Test
    void shouldReturnOnlyCardsByUser1() {
        List<Card> cards = cardRepository.findByOwnerId(user1.getId());

        assertThat(cards).isNotNull();
        assertThat(cards).hasSize(2);
        assertThat(cards).extracting(Card::getId)
                .containsExactlyInAnyOrder(card1.getId(), card2.getId());
        assertThat(cards).allMatch(card -> card.getOwner().getId().equals(user1.getId()));
    }

    @Test
    void shouldReturnOnlyCardsByUser2() {
        List<Card> cards = cardRepository.findByOwnerId(user2.getId());

        assertThat(cards).isNotNull();
        assertThat(cards).hasSize(1);
        assertThat(cards.get(0).getCardNumber()).isEqualTo("4000-3333-3333-3333");
    }

    @Test
    void shouldReturnCardByCardNumber() {
        Optional<Card> foundCard = cardRepository.findByCardNumber("4000-1111-1111-1111");

        assertThat(foundCard).isPresent();
        assertThat(foundCard.get().getId()).isEqualTo(card1.getId());
    }

}
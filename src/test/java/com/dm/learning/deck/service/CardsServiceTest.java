package com.dm.learning.deck.service;

import com.dm.learning.dto.card.CardDto;
import com.dm.learning.entities.Card;
import com.dm.learning.entities.Deck;
import com.dm.learning.entities.Type;
import com.dm.learning.exceptions.InvalidDataException;
import com.dm.learning.repositories.CardRepository;
import com.dm.learning.repositories.DeckRepository;
import com.dm.learning.services.implementation.CardsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardsServiceTest {

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private CardRepository repository;

    @InjectMocks
    private CardsServiceImpl service;

    private UUID cardId;
    private UUID cardId2;
    private Card testCard;
    private Card testCard2;
    private Deck testDeck;
    private Set<Card> testDeckCards;
    private Double initialRate = 1.0;
    private Double defaultRate = 1.0;

    @Mock
    private Type type;

    @BeforeEach
    void setUp() {
        cardId = UUID.randomUUID();
        cardId2 = UUID.randomUUID();

        testCard = new Card();
        testCard.setId(cardId);
        testCard.setTitle("Title");
        testCard.setRate(initialRate);
        testCard.setProbability(0.5);
        testCard.setTimesDisplayed(0);

        testCard2 = new Card();
        testCard2.setId(cardId2);
        testCard2.setTitle("Title2");
        testCard2.setRate(initialRate);
        testCard2.setProbability(0.5);
        testCard2.setTimesDisplayed(0);

        testDeck = new Deck();
        testDeck.setId(UUID.randomUUID());
        testDeck.setType(type);
        testDeckCards = new HashSet<>(Set.of(testCard, testCard2));
        testDeck.setCards(testDeckCards);

        testCard.setDeck(testDeck);
        testCard2.setDeck(testDeck);

        when(repository.findCardById(cardId)).thenReturn(Optional.of(testCard));
    }

    @Test
    void updateCardsWeight_HardWeight_UpdatesRateAndProbability() {
        String jsonWeightType = "{\"weightType\":\"Hard\"}";
        mockRepositories();

        List<CardDto> result = service.updateCardsWeight(cardId, jsonWeightType);

        assertEquals(2, result.size());
        CardDto updatedCard = result.get(0);

        // Verify rate was adjusted (HARD.getWeight() = 1.5, MAX.getWeight() = 10)
        assertEquals(1.5, updatedCard.getRate());

        // Verify probability was recalculated
        assertEquals(0.6, updatedCard.getProbability());

        // Verify timesDisplayed incremented
        assertEquals(1, updatedCard.getTimesDisplayed());

        // Verify rate didn't change
        assertEquals(initialRate, testCard2.getRate());

        // Verify probability was recalculated
        assertEquals(0.4, testCard2.getProbability());
    }

    @Test
    void updateCardsWeight_EasyWeight_ReducesRate() {
        String jsonWeightType = "{\"weightType\":\"Easy\"}";
        mockRepositories();

        List<CardDto> result = service.updateCardsWeight(cardId, jsonWeightType);

        assertTrue(result.get(0).getRate() < initialRate); // Rate should decrease
    }

    @Test
    void updateCardsWeight_InvalidWeightType_SetsDefaultRate() {
        String jsonWeightType = "{\"weightType\":\"Invalid\"}";
        mockRepositories();

        List<CardDto> result = service.updateCardsWeight(cardId, jsonWeightType);

        assertEquals(defaultRate, result.get(0).getRate());
    }

    @Test
    void updateCardsWeight_CardNotFound_ThrowsException() {
        when(repository.findCardById(cardId)).thenReturn(Optional.empty());
        String jsonWeightType = "{\"weightType\":\"Medium\"}";

        assertThrows(InvalidDataException.class, () ->
                service.updateCardsWeight(cardId, jsonWeightType)
        );
    }

    @Test
    void updateCardsWeight_DeckNotFound_ThrowsException() {
        when(deckRepository.findById(any())).thenReturn(Optional.empty());
        String jsonWeightType = "{\"weightType\":\"Medium\"}";

        assertThrows(InvalidDataException.class, () ->
                service.updateCardsWeight(cardId, jsonWeightType)
        );
    }

    @Test
    void calculateDecayFactor_OldLastDisplayed_ReturnsLowValue() {
        String jsonWeightType = "{\"weightType\":\"Hard\"}";
        mockRepositories();

        testCard2.setTimesDisplayed(1);
        testCard2.setLastDisplayed(Instant.now().minus(Duration.ofDays(10)));

        List<CardDto> result = service.updateCardsWeight(cardId, jsonWeightType);

        CardDto updatedTestCard2 = result.get(1);

        // DecayFactor = Math.exp(-0.1 * -10) = 2.718281828459045
        var decayFactor = 2.718281828459045;
        var newRate = decayFactor * initialRate;

        assertEquals(newRate, updatedTestCard2.getRate());
    }

    @Test
    void updateCardsWeight_SavesCardAndDeck() {
        String jsonWeightType = "{\"weightType\":\"Medium\"}";
        mockRepositories();

        service.updateCardsWeight(cardId, jsonWeightType);

        verify(repository, times(1)).findCardById(any());
        verify(deckRepository, times(1)).findById(any());
        verify(repository, times(1)).saveAndFlush(any(Card.class));
        verify(repository, times(1)).saveAll(anySet());
    }

    private void mockRepositories() {
        when(deckRepository.findById(testDeck.getId())).thenReturn(Optional.of(testDeck));
        when(repository.saveAndFlush(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(repository.saveAll(anySet())).thenAnswer(invocation -> new ArrayList<>(invocation.getArgument(0)));
    }
}

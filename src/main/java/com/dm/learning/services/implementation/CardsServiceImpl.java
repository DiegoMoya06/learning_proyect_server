package com.dm.learning.services.implementation;

import com.dm.learning.dto.card.CardDto;
import com.dm.learning.entities.Card;
import com.dm.learning.exceptions.InvalidDataException;
import com.dm.learning.repositories.CardRepository;
import com.dm.learning.repositories.DeckRepository;
import com.dm.learning.services.CardService;
import com.dm.learning.services.implementation.base.BaseServiceImpl;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.dm.learning.entities.WeightValue.*;

@Service
public class CardsServiceImpl extends BaseServiceImpl<CardRepository> implements CardService {

    private final DeckRepository deckRepository;

    private static final Double DECAY_CONSTANT = 0.1;

    protected CardsServiceImpl(CardRepository repository, DeckRepository deckRepository) {
        super(repository);
        this.deckRepository = deckRepository;
    }

    @Override
    public List<CardDto> updateCardsWeight(UUID cardId, String jsonWeightType) {
        logger.info("Updating Cards weight");

        JSONObject json = new JSONObject(jsonWeightType);
        String weightType = json.getString("weightType");

        var dbCard = repository.findCardById(cardId)
                .orElseThrow(() -> new InvalidDataException("Could not find Card with id: {}", cardId));
        var dbDeck = deckRepository.findById(dbCard.getDeck().getId())
                .orElseThrow(() -> new InvalidDataException("Could not find Deck with id: {}", dbCard.getDeck().getId()));
        var dbDeckCards = Optional.ofNullable(dbDeck.getCards()).orElse(Set.of());

        var totalRate = dbDeckCards.stream()
                .reduce(0.0, (sum, card) -> sum + card.getRate(), Double::sum);

        var newRate = setNewRate(weightType, dbCard.getRate());
        var newTotalRate = totalRate - dbCard.getRate() + newRate;
        var newProbability = newRate / newTotalRate;
        var newTimesDisplayed = dbCard.getTimesDisplayed() + 1;
        var currentDate = Instant.now();
//        TODO: add logged user
        var currentUser = "Test-User";

        dbCard.setRate(newRate);
        dbCard.setProbability(newProbability);
        dbCard.setTimesDisplayed(newTimesDisplayed);
        dbCard.setLastDisplayed(currentDate);
        dbCard.setUpdatedBy(currentUser);

        var updated = repository.saveAndFlush(dbCard);

        dbDeckCards.removeIf(card -> card.getId() == updated.getId());
        dbDeckCards.add(updated);

        calculateAdjustedRates(dbDeckCards);
        calculateProbabilities(dbDeckCards);

        repository.saveAll(dbDeckCards);

        return CardDto.fromEntitiesAsList(dbDeckCards);
    }

    private double setNewRate(String weightType, double rate) {
        switch (weightType) {
            case "Hard" -> rate = Math.min((rate * HARD.getWeight()), MAX.getWeight());
            case "Medium" -> rate = Math.min((rate * MEDIUM.getWeight()), MAX.getWeight());
            case "Easy" -> rate = Math.min((rate * EASY.getWeight()), MIN.getWeight());
            default -> rate = 1;
        }

        return rate;
    }

    private double calculateDecayFactor(Instant lastDisplayed) {
        var currentTime = Instant.now();
        Duration elapsedTime = Duration.between(currentTime, lastDisplayed);

        return Math.exp(-DECAY_CONSTANT * elapsedTime.toDays());
    }

    private void calculateAdjustedRates(Set<Card> cardList) {
        for (var card : cardList) {
            var cardLastDisplayed = Optional.ofNullable(card.getLastDisplayed()).orElse(Instant.now());
            var decayFactor = calculateDecayFactor(cardLastDisplayed);
            var adjustedRate = card.getRate() * decayFactor;
            card.setRate(adjustedRate);
        }
    }

    private void calculateProbabilities(Set<Card> cardList) {
        var totalAdjustedRate = cardList.stream()
                .reduce(0.0, (sum, card) -> sum + card.getRate(), Double::sum);

        for (var card : cardList) {
            var probability = card.getRate() / totalAdjustedRate;
            card.setProbability(probability);
        }
    }
}

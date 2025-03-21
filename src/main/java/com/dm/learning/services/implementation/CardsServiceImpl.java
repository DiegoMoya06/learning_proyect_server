package com.dm.learning.services.implementation;

import com.dm.learning.dto.card.CardDto;
import com.dm.learning.exceptions.InvalidDataException;
import com.dm.learning.repositories.CardRepository;
import com.dm.learning.repositories.DeckRepository;
import com.dm.learning.services.CardService;
import com.dm.learning.services.implementation.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static com.dm.learning.entities.WeightValue.*;

@Service
public class CardsServiceImpl extends BaseServiceImpl<CardRepository> implements CardService {

    private final DeckRepository deckRepository;

    protected CardsServiceImpl(CardRepository repository, DeckRepository deckRepository) {
        super(repository);
        this.deckRepository = deckRepository;
    }

    @Override
    public List<CardDto> updateCardsWeight(String cardTitle, String weightType) {
        logger.info("Updating Cards weight");

        var dbCard = repository.findCardByTitle(cardTitle)
                .orElseThrow(() -> new InvalidDataException("Could not find Card with name: {}", cardTitle));
        var dbDeck = deckRepository.findDeckByName(dbCard.getDeck().getName())
                .orElseThrow(() -> new InvalidDataException("Could not find Deck with name: {}", dbCard.getDeck().getName()));
        var totalRate = dbDeck.getCards().stream()
                .reduce(0.0, (sum, card) -> sum + card.getRate(), Double::sum);

        var newRate = setNewRate(weightType, dbCard.getRate());
        var newTotalRate = totalRate - dbCard.getRate() + newRate;
        var newProbability = newRate / newTotalRate;
        var newTimesDisplayed = dbCard.getTimesDisplayed() + 1;
        var currentDate = Instant.now();
        var currentUser = "Test-User";

        dbCard.setRate(newRate);
        dbCard.setProbability(newProbability);
        dbCard.setTimesDisplayed(newTimesDisplayed);
        dbCard.setLastDisplayed(currentDate);
        dbCard.setUpdated(currentDate);
        dbCard.setUpdatedBy(currentUser);

        var updated = repository.saveAndFlush(dbCard);

        dbDeck.getCards().removeIf(card -> card.getId() == updated.getId());
        dbDeck.getCards().add(updated);

        return CardDto.fromEntitiesAsList(dbDeck.getCards());
    }

    private double setNewRate(String weightType, double rate) {
        switch (weightType) {
            case "Hard" -> rate = Math.min((rate * HARD.getWeight()), MAX.getWeight());
            case "Medium" -> rate = Math.min((rate * MEDIUM.getWeight()), MAX.getWeight());
            case "Easy" -> rate = Math.min((rate * EASY.getWeight()), MIN.getWeight());
        }

        return rate;
    }
}

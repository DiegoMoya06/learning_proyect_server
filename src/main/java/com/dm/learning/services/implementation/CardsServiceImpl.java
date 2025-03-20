package com.dm.learning.services.implementation;

import com.dm.learning.dto.card.CardDto;
import com.dm.learning.exceptions.InvalidDataException;
import com.dm.learning.repositories.CardRepository;
import com.dm.learning.repositories.DeckRepository;
import com.dm.learning.services.CardService;
import com.dm.learning.services.implementation.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CardsServiceImpl extends BaseServiceImpl<CardRepository> implements CardService {

    private final DeckRepository deckRepository;

    protected CardsServiceImpl(CardRepository repository, DeckRepository deckRepository) {
        super(repository);
        this.deckRepository = deckRepository;
    }

    @Override
    public List<CardDto> updateCardsWeight(UUID cardId, String weightType) {
        logger.info("Updating Cards weight");

        var toUpdate = repository.getCardById(cardId)
                .orElseThrow(() -> new InvalidDataException("Could not find Card with id: {}", cardId));
        var dbDeck = deckRepository.findDeckById(toUpdate.getDeck().getId());

        return null;
    }
}

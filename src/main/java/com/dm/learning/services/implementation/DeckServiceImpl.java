package com.dm.learning.services.implementation;

import com.dm.learning.dto.deck.DeckDto;
import com.dm.learning.entities.Deck;
import com.dm.learning.repositories.DeckRepository;
import com.dm.learning.services.DeckService;
import com.dm.learning.services.implementation.base.BaseServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DeckServiceImpl extends BaseServiceImpl<DeckRepository> implements DeckService {

    protected DeckServiceImpl(DeckRepository repository) {
        super(repository);
    }

    @Override
    @Transactional
    public List<DeckDto> getAllDecks() {
        Comparator<Deck> deckNameComparator = Comparator.comparing(Deck::getName);

        var entities = repository.findAllWithType();
        entities.sort(deckNameComparator);

        return DeckDto.fromEntitiesAsList(entities);
    }
}

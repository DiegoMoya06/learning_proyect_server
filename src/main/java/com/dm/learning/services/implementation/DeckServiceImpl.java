package com.dm.learning.services.implementation;

import com.dm.learning.dto.deck.DeckDto;
import com.dm.learning.repositories.DeckRepository;
import com.dm.learning.services.DeckService;
import com.dm.learning.services.implementation.base.BaseServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DeckServiceImpl extends BaseServiceImpl<DeckRepository> implements DeckService {
    protected DeckServiceImpl(DeckRepository repository) {
        super(repository);
    }

    @Override
    public List<DeckDto> getAllDecks() {
        var entities = repository.findAll();

        return DeckDto.fromEntitiesAsList(entities);
    }
}

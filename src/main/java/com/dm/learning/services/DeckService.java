package com.dm.learning.services;

import com.dm.learning.dto.deck.DeckDto;
import com.dm.learning.services.base.BaseService;

import java.util.List;

public interface DeckService extends BaseService {
    List<DeckDto> getAllDecks();
}

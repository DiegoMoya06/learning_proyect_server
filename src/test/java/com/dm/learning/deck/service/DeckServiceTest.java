package com.dm.learning.deck.service;

import com.dm.learning.dto.deck.DeckDto;
import com.dm.learning.entities.Deck;
import com.dm.learning.entities.Type;
import com.dm.learning.repositories.DeckRepository;
import com.dm.learning.services.implementation.DeckServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeckServiceTest {
    @Mock
    private DeckRepository repository;

    @InjectMocks
    private DeckServiceImpl deckService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Disabled("To be checked")
    @Test
    void testGetAllDecks() {
        Type type = new Type(UUID.randomUUID(),"Type 1",Set.of());
        Deck deck1 = new Deck(UUID.randomUUID(), "Deck A", "Test Description", type, Set.of());
        Deck deck2 = new Deck(UUID.randomUUID(), "Deck B", "Test Description 2", type, Set.of());
        List<Deck> decks = List.of(deck2, deck1);

        when(repository.findAllWithType()).thenReturn(new ArrayList<>(decks));

        List<DeckDto> result = deckService.getAllDecks();
        // Debugging: Print the result
        System.out.println("Result: " + result);

        assertEquals(2, result.size());
        assertEquals("Deck A", result.get(0).getName());
        assertEquals("Deck B", result.get(1).getName());

        verify(repository, times(1)).findAllWithType();
    }
}

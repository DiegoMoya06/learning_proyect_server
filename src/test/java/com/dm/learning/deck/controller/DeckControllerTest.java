package com.dm.learning.deck.controller;

import com.dm.learning.controllers.DeckController;
import com.dm.learning.services.DeckService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeckControllerTest {

    @InjectMocks
    DeckController controller;

    @Mock
    DeckService service;

    @Test
    void testGetAllDecks() {
        controller.getAll();
        verify(service).getAllDecks();
    }
}

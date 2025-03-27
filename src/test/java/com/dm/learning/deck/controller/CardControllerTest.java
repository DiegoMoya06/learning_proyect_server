package com.dm.learning.deck.controller;

import com.dm.learning.controllers.CardController;
import com.dm.learning.services.CardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {
    @InjectMocks
    CardController controller;

    @Mock
    CardService service;

    @Test
    void updateCardsWeight() {
        UUID id = UUID.randomUUID();
        String jsonString = "{\"weightType\":\"Hard\"}";
        controller.updateCardsWeight(id, jsonString);
        verify(service).updateCardsWeight(id, jsonString);
    }
}

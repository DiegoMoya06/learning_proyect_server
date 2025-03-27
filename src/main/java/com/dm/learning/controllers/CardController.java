package com.dm.learning.controllers;

import com.dm.learning.controllers.base.BaseController;
import com.dm.learning.dto.card.CardDto;
import com.dm.learning.services.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/cards")
public class CardController extends BaseController<CardService> {
    protected CardController(CardService service) {
        super(service);
    }

    /**
     * update endpoint | PUT
     *
     * @param id         uuid of the card
     * @param weightType string weightType
     * @return updated cardList
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping(path = "/{id}")
    public ResponseEntity<List<CardDto>> updateCardsWeight(@PathVariable UUID id, @RequestBody String weightType) {
        logger.info("CardController was called with cardId {} and weightType {}.", id, weightType);
        return ResponseEntity.ok(service.updateCardsWeight(id, weightType));
    }
}

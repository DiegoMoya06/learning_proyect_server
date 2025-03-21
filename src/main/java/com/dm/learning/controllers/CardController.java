package com.dm.learning.controllers;

import com.dm.learning.controllers.base.BaseController;
import com.dm.learning.dto.card.CardDto;
import com.dm.learning.services.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/cards")
public class CardController extends BaseController<CardService> {
    protected CardController(CardService service) {
        super(service);
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping(path = "/{cardTitle}")
    public ResponseEntity<List<CardDto>> updateCardsWeight(@PathVariable String cardTitle, @RequestBody String weightType) {
        logger.info("CardController was called");
        return ResponseEntity.ok(service.updateCardsWeight(cardTitle, weightType));
    }
}

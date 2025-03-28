package com.dm.learning.controllers;

import com.dm.learning.controllers.base.BaseController;
import com.dm.learning.dto.deck.DeckDto;
import com.dm.learning.services.DeckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/decks")
public class DeckController extends BaseController<DeckService> {
    protected DeckController(DeckService service) {
        super(service);
    }

    /**
     * getAll endpoint | GET
     *
     * @return allDecks
     */
    @GetMapping(path = "")
    public ResponseEntity<List<DeckDto>> getAll() {
        logger.info("DeckController was called");
        return ResponseEntity.ok(service.getAllDecks());
    }
}

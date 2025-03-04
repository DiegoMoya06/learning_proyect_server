package com.dm.learning.deck.controller;

import com.dm.learning.controllers.DSController;
import com.dm.learning.dto.card.NewCardDto;
import com.dm.learning.dto.deck.NewAutomaticDeckDto;
import com.dm.learning.services.DSService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DSControllerTest {

    @Mock
    private DSService dsService;

    @InjectMocks
    private DSController dsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendInputFromFile() throws IOException {
        MultipartFile file = new MockMultipartFile("file",
                "test.pdf", "application/pdf", "test-data".getBytes());
        var testId = UUID.randomUUID();
        var newCardDto = new NewCardDto();
        var expectedNewDeck = new NewAutomaticDeckDto(testId, "Description test", List.of(newCardDto));

        when(dsService.sendChatRequestWithFile(any(File.class))).thenReturn(expectedNewDeck);

        ResponseEntity<NewAutomaticDeckDto> actualResponse = dsController.sendInputFromFile(file);

        ResponseEntity<NewAutomaticDeckDto> expectedResponse = ResponseEntity.ok(expectedNewDeck);

        assertEquals(expectedResponse, actualResponse);
    }


}

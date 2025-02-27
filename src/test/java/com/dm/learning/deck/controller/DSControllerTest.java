package com.dm.learning.deck.controller;

import com.dm.learning.controllers.DSController;
import com.dm.learning.services.DSService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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
        String expectedResponse = "Mocked response from service";
        when(dsService.sendChatRequestWithFile(any(File.class))).thenReturn(expectedResponse);

        String actualResponse = dsController.sendInputFromFile(file);

        assertEquals(expectedResponse, actualResponse);
    }
}

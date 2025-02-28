package com.dm.learning.deck.service;

import com.dm.learning.services.PdfService;
import com.dm.learning.services.implementation.DSServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

import static com.dm.learning.utils.Utils.createMockPdfFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DSServiceImplTest {
    @Mock
    private PdfService pdfService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DSServiceImpl dsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendChatRequestWithFile() throws IOException {
//        TODO: solve error here
        // Arrange
        File pdfFile = createMockPdfFile();
        assertTrue(pdfFile.length() > 0, "The PDF file should not be empty");
        File file = new File("test.pdf");
        String extractedText = "Extracted text from PDF";
        String apiResponse = "Mocked API response";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(apiResponse, HttpStatus.OK);
        when(pdfService.extractTextFromPdf(pdfFile)).thenReturn(extractedText);
        when(restTemplate.exchange(anyString(), any(), any(), any(Class.class)))
                .thenReturn(mockResponse);

        // Act
        String actualResponse = dsService.sendChatRequestWithFile(pdfFile);

        // Assert
        assertEquals(apiResponse, actualResponse);
    }
}

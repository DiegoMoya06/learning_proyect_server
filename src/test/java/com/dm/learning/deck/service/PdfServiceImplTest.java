package com.dm.learning.deck.service;

import com.dm.learning.services.implementation.PdfServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;

import static com.dm.learning.utils.Utils.createMockPdfFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PdfServiceImplTest {

    @InjectMocks
    private PdfServiceImpl pdfService;

    @Test
    public void testExtractTextFromPdf() throws IOException {
        String expectedText = "PDF Text example\n";

        File pdfFile = createMockPdfFile();
        assertTrue(pdfFile.length() > 0, "The PDF file should not be empty");

        String result = pdfService.extractTextFromPdf(pdfFile);
        assertEquals(expectedText.replace("\r\n", "\n"),
                result.replace("\r\n", "\n"));
    }
}

package com.dm.learning.deck.service;

import com.dm.learning.dto.deck.NewAutomaticDeckDto;
import com.dm.learning.entities.Choice;
import com.dm.learning.entities.ResponseMessage;
import com.dm.learning.exceptions.ApiRequestException;
import com.dm.learning.services.PdfService;
import com.dm.learning.services.implementation.DSServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.dm.learning.utils.Utils.createMockPdfFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DSServiceImplTest {
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

    @Value("${LC_DS_CHAT_API_URL}")
    private String API_URL;

    @Test
    void testTransformJsonToCardList_Success() throws IOException {
        String jsonContent = "```json {\"generalDescription\": \"Test description\", \"cardsList\": []} ```";
        Choice mockChoice = mock(Choice.class);
        ResponseMessage mockMessage = mock(ResponseMessage.class);

        when(mockChoice.getMessage()).thenReturn(mockMessage);
        when(mockMessage.getContent()).thenReturn(jsonContent);

        NewAutomaticDeckDto result = dsService.transformJsonToCardList(List.of(mockChoice));

        assertNotNull(result);
        assertEquals("Test description", result.getGeneralDescription());
    }

    @Test
    void testTransformJsonToCardList_InvalidJson() {
        String jsonContent = "```json {invalidJson} ```";
        Choice mockChoice = mock(Choice.class);
        ResponseMessage mockMessage = mock(ResponseMessage.class);

        when(mockChoice.getMessage()).thenReturn(mockMessage);
        when(mockMessage.getContent()).thenReturn(jsonContent);

        assertThrows(IOException.class, () -> dsService.transformJsonToCardList(List.of(mockChoice)));
    }

    @Test
    void testTransformJsonToCardList_NoJsonFound() {
        String contentWithoutJson = "No JSON text";
        Choice mockChoice = mock(Choice.class);
        ResponseMessage mockMessage = mock(ResponseMessage.class);

        when(mockChoice.getMessage()).thenReturn(mockMessage);
        when(mockMessage.getContent()).thenReturn(contentWithoutJson);

        assertThrows(IOException.class, () -> dsService.transformJsonToCardList(List.of(mockChoice)));
    }

    @Test
    void testSendChatRequestWithFile() throws IOException {

        File pdfFile = createMockPdfFile();
        assertTrue(pdfFile.length() > 0, "The PDF file should not be empty");

        String mockApiResponse = "{\"choices\": [{\"message\": {\"content\": \"```json {\\\"generalDescription\\\": \\\"Description test\\\", \\\"cardsList\\\": []} ```\"}}]}";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockApiResponse, HttpStatus.OK);

        doReturn(mockResponse).when(restTemplate).exchange(
                eq(API_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        );

        NewAutomaticDeckDto actualResponse = dsService.sendChatRequestWithFile(pdfFile);

        assertNotNull(actualResponse);
        assertEquals("Description test", actualResponse.getGeneralDescription());
    }

    @Test
    void testSendChatRequestWithFile_EmptyFile() {
        File mockFile = mock(File.class);
        when(mockFile.length()).thenReturn(0L);

        assertThrows(IOException.class, () -> dsService.sendChatRequestWithFile(mockFile));
    }

    @Test
    void testSendChatRequestWithFile_ApiFails(){
        File mockFile = mock(File.class);
        when(mockFile.length()).thenReturn(100L);

        assertThrows(ApiRequestException.class, () -> dsService.sendChatRequestWithFile(mockFile));
    }
}

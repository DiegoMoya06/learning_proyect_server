package com.dm.learning.services.implementation;

import com.dm.learning.services.CGService;
import com.dm.learning.services.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CGServiceImpl implements CGService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PdfService pdfService;

    @Value("${LC_CG_CHAT_API_URL}")
    private String API_URL;
    @Value("${LC_CG_FILE_API_URL}")
    private String FILE_API_URL;
    @Value("${LC_C_API_KEY}")
    private String LC_C_API_KEY;

    private final String INPUT = """
            I would like you to check this document, understand the main idea and rewrite the content on multiple\s
            objects with this format:\s
                {
                     title: string;
                     description: string;
                     rate: number;
                     probability: number;
                }
            The purpose of this is to create objects to study different topics and concepts from the document, in\s
            every object, the Title will be the main idea or concept and the Description will be a short but well\s
            explained description of the concept. Assign a value of 1.0 to the rate of every new object. The\s
            probability for every object will be calculated dividing the rate of the object between the sum of all the rates.\s
                        
            """;

    @Autowired
    private RestTemplate restTemplate;

    public CGServiceImpl(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @Override
    public String sendChatRequestWithFile(File file) throws IOException {
        var documentText = pdfService.extractTextFromPdf(file);
        logger.info(documentText);
        var requestEntity = createMsgRequest(INPUT + documentText);

        ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, Map.class);
        return response.getBody().toString();
    }

    @Override
    public String sendChatRequest(String userMessage) {
        var requestEntity = createMsgRequest(userMessage);

        ResponseEntity<Map> response = restTemplate.exchange(API_URL, HttpMethod.POST, requestEntity, Map.class);
        return response.getBody().toString();
    }

    private HttpEntity<Map<String, Object>> createMsgRequest(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(LC_C_API_KEY);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", message)));

        return new HttpEntity<>(requestBody, headers);
    }

    @Override
    public String uploadFile(File file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(LC_C_API_KEY);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(file));
        body.add("purpose", "assistants");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(FILE_API_URL, HttpMethod.POST, requestEntity, Map.class);

        // Extract file_id from response
        Map<String, Object> responseBody = response.getBody();
        return responseBody != null ? (String) responseBody.get("id") : null;
    }
}

package com.dm.learning.services.implementation;

import com.dm.learning.services.DSService;
import com.dm.learning.services.PdfService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@Service
public class DSServiceImp implements DSService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PdfService pdfService;

    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";

    private String LC_DS_API_KEY = "api-key";

    private final String INPUT = "I have extracted text from a PDF, and I need help structuring it into study-friendly concepts and descriptions. Please analyze the text and organize it as follows:\\n" +
            "\\n" +
            "If the document contains vocabulary (e.g., words and translations):\\n" +
            "\\n" +
            "Extract each word or phrase as a 'Concept.'\\n" +
            "Provide its translation or meaning as the 'Description.'\\n" +
            "If the document is about a specific topic:\\n" +
            "\\n" +
            "Identify the main concept and provide a brief description.\\n" +
            "Extract relevant subconcepts and their descriptions in a structured, hierarchical format.\\n" +
            "\\n" +
            "then rewrite the content on all the necessary objects with this format {\\n" +
            " title: string;\\n" +
            " description: string;\\n" +
            " rate: number;\\n" +
            " probability: number;\\n" +
            " }\\n" +
            "\\n" +
            "Please avoid to add into the objects irrelevant info like who wrote the pdf, editorial information, table of content in case is a book, index of books, copyright page, dedication, bibliography, etc.\\n" +
            "\\n" +
            "Assign a value of 1.0 to the rate of every new object. The probability for every object will be calculated dividing the rate of the object between the sum of all the rates. \\n" +
            "\\n" +
            "this is the text:\\n";
    public DSServiceImp(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String sendChatRequestWithFile(File file) throws IOException {
        var documentText = pdfService.extractTextFromPdf(file);
        logger.info(documentText);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + LC_DS_API_KEY);
        headers.set("Content-Type", "application/json");

        JSONArray messages = new JSONArray();
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", INPUT + documentText);
        messages.put(userMessage);

        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put("model","deepseek-chat");
        requestBodyJson.put("messages", messages);
        var stringRequestBody = requestBodyJson.toString();

        HttpEntity<String> entity = new HttpEntity<>(stringRequestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}

package com.dm.learning.services.implementation;

import com.dm.learning.exceptions.ApiRequestException;
import com.dm.learning.exceptions.FileProcessingException;
import com.dm.learning.services.DSService;
import com.dm.learning.services.PdfService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@Service
public class DSServiceImpl implements DSService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PdfService pdfService;

    private final RestTemplate restTemplate;

    @Value("${LC_DS_CHAT_API_URL}")
    private String API_URL;

    @Value("${LC_DS_API_KEY}")
    private String LC_DS_API_KEY;

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

    public DSServiceImpl(PdfService pdfService, RestTemplate restTemplate) {
        this.pdfService = pdfService;
        this.restTemplate = restTemplate;
    }

    @Override
    public String sendChatRequestWithFile(File file) throws IOException {
        logger.info("sendChatRequestWithFile function was called");
        logger.info(file.toString());
        if (file.length() == 0) {
            throw new IOException("File is empty");
        }

        try {
            var documentText = pdfService.extractTextFromPdf(file);


            HttpHeaders headers = new HttpHeaders();
//            TODO: change
//            headers.set("Authorization", "Bearer " + LC_DS_API_KEY);
            headers.set("Authorization", "Bearer " + LC_DS_API_KEY);
            headers.set("Content-Type", "application/json");

            JSONArray messages = new JSONArray();
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", INPUT + documentText);
            messages.put(userMessage);

            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("model", "deepseek-chat");
            requestBodyJson.put("messages", messages);
            var stringRequestBody = requestBodyJson.toString();

            HttpEntity<String> entity = new HttpEntity<>(stringRequestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            System.out.println(response);

//            TODO: clean response to only send back the card objects
            return response.getBody();
        } catch (IOException e) {
            throw new FileProcessingException("Failed to process the file: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ApiRequestException("Failed to send API request: " + e.getMessage(), e);
        }
    }
}

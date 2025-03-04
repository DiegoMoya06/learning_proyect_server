package com.dm.learning.services.implementation;

import com.dm.learning.dto.deck.NewAutomaticDeckDto;
import com.dm.learning.entities.Choice;
import com.dm.learning.entities.DSResponse;
import com.dm.learning.exceptions.ApiRequestException;
import com.dm.learning.exceptions.FileProcessingException;
import com.dm.learning.services.DSService;
import com.dm.learning.services.PdfService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DSServiceImpl implements DSService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PdfService pdfService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${LC_DS_CHAT_API_URL}")
    private String API_URL;

    @Value("${LC_DS_API_KEY}")
    private String LC_DS_API_KEY;

    private final String INPUT = "I have extracted text from a PDF, and I need help structuring it into \\n" +
            "study-friendly concepts and descriptions. Please analyze the text and organize it as follows:" +
            "First add a general description to tell what is the text about, specifying it with MAIN_DESCRIPTION:, then \\n" +
            "\\n" +
            "if the document contains vocabulary (individual words or phrases with translations):\\n" +
            "\\n" +
            "Extract each word or phrase as a 'Concept.'\\n" +
            "Provide its translation or meaning as the 'Description.'\\n" +
            "If the document contains full sentences with translations:\\n" +
            "\\n" +
            "Treat the entire sentence as the 'Concept.'\\n" +
            "Treat the corresponding translation as the 'Description.'\\n" +
            "If the document is about a specific topic (not language learning):\\n" +
            "\\n" +
            "Identify the main concept.\\n" +
            "Extract relevant concepts and subconcepts, and add their descriptions, every concept and subconcept will be an object.\\n" +
            "Ensure that the output is clear, concise, and optimized for studying. \\n" +
            "\\n" +
            "Then rewrite the content on all the necessary objects with this format {\\n" +
            " title: string;\\n" +
            " description: string;\\n" +
            " rate: number;\\n" +
            " probability: number;\\n" +
            " }\\n" +
            "\\n" +
            "At the end the output will be an object with the following structure: " +
            "{\n" +
            "  \"generalDescription\": \"Brief summary of the document\",\n" +
            "  \"cardsList\": [\n" +
            "    {\n" +
            "      \"title\": \"Concept or sentence\",\n" +
            "      \"description\": \"Explanation or translation\",\n" +
            "      \"rate\": 1.0,\n" +
            "      \"probability\": 0.0\n" +
            "    }\n" +
            "  ]\n" +
            "}\n" +
            "where the generalDescription should provide a brief summary of the document's content. " +
            "the cardsList should include all extracted concepts with their corresponding descriptions" +
            "Please avoid to add into the objects irrelevant info like who wrote the pdf, editorial information, table of content in case is a book, index of books, copyright page, dedication, bibliography, etc.\\n" +
            "\\n" +
            "Assign a value of 1.0 to the rate of every new object. The probability for every object will be calculated dividing the rate of the object between the sum of all the rates. \\n" +
            "\\n" +
            "this is the text:\\n";

    public DSServiceImpl(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @Override
    public NewAutomaticDeckDto sendChatRequestWithFile(File file) throws IOException {
        logger.info("sendChatRequestWithFile function was called");

        if (file.length() == 0) {
            var errorMsg = "File is empty";
            logger.error(errorMsg);
            throw new IOException(errorMsg);
        }

        try {
            var documentText = pdfService.extractTextFromPdf(file);

            HttpHeaders headers = new HttpHeaders();
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

            ObjectMapper objectMapper = new ObjectMapper();
            DSResponse dsResponse = objectMapper.readValue(response.getBody(), DSResponse.class);

            return transformJsonToCardList(dsResponse.getChoices());
        } catch (IOException e) {
            throw new FileProcessingException("Failed to process the file: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ApiRequestException("Failed to send API request: " + e.getMessage(), e);
        }
    }

    public NewAutomaticDeckDto transformJsonToCardList(List<Choice> choices) throws IOException {
        Pattern pattern = Pattern.compile("```json\\s*(\\{.*?\\})\\s*```", Pattern.DOTALL);

        var choice = choices.get(0);
        var content = choice.getMessage().getContent();
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String jsonText = matcher.group(1);

            try {
                ObjectMapper objectMapper = new ObjectMapper();

                return objectMapper.readValue(jsonText, new TypeReference<>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Failed to parse JSON into NewAutomaticDeckDto", e);
            }
        } else {
            var exceptionMsg = "No JSON found in the text.";
            logger.error(exceptionMsg);
            throw new IOException(exceptionMsg);
        }
    }
}

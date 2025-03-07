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
            // TestData to avoid calling always the API
//            String responseBody = "{\"id\":\"c8b32458-1ad1-4d0d-bc5b-b6d6ec500582\",\"object\":\"chat.completion\",\"created\":1741163670,\"model\":\"deepseek-chat\",\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\"Based on the provided text, here is the structured output following your requested format:\\n\\n```json\\n{\\n  \\\"generalDescription\\\": \\\"This document is a practical manual for learning basic German vocabulary. It contains over 6,500 words and expressions organized into 66 thematic chapters. Each chapter is divided into subtopics, providing vocabulary, phrases, and contextual examples to help learners understand and use the language effectively. The text also includes notes on grammar, false friends, and translation tips.\\\",\\n  \\\"cardsList\\\": [\\n    {\\n      \\\"title\\\": \\\"scheinen*\\\",\\n      \\\"description\\\": \\\"parecer, tener aspecto\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"wiegen*\\\",\\n      \\\"description\\\": \\\"pesar\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"beschreiben*\\\",\\n      \\\"description\\\": \\\"describir\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"ziemlich\\\",\\n      \\\"description\\\": \\\"bastante\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"sehr\\\",\\n      \\\"description\\\": \\\"muy\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"zu\\\",\\n      \\\"description\\\": \\\"demasiado\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"die Beschreibung (-en)\\\",\\n      \\\"description\\\": \\\"descripción\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"die Erscheinung (-en)\\\",\\n      \\\"description\\\": \\\"apariencia\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"das Aussehen\\\",\\n      \\\"description\\\": \\\"aspecto\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"die Haltung (-en)\\\",\\n      \\\"description\\\": \\\"porte\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"der Gang (Gänge)\\\",\\n      \\\"description\\\": \\\"modo de andar\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"die Größe (-n)\\\",\\n      \\\"description\\\": \\\"talla, estatura\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"das Gewicht (-e)\\\",\\n      \\\"description\\\": \\\"peso\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"das Geschlecht (-er)\\\",\\n      \\\"description\\\": \\\"sexo\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"das Gesicht (-er)\\\",\\n      \\\"description\\\": \\\"cara\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"das Haar (-e)\\\",\\n      \\\"description\\\": \\\"pelo, cabello\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"die Stirn (-en)\\\",\\n      \\\"description\\\": \\\"frente\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"der Bart (Bärte)\\\",\\n      \\\"description\\\": \\\"barba\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"der Schnurrbart (-bärte)\\\",\\n      \\\"description\\\": \\\"bigote\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"die Nase (-n)\\\",\\n      \\\"description\\\": \\\"nariz\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"das Auge (-n)\\\",\\n      \\\"description\\\": \\\"ojo\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"die Haut\\\",\\n      \\\"description\\\": \\\"piel\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"die Lippe (-n)\\\",\\n      \\\"description\\\": \\\"labios\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"der Mund (Münder)\\\",\\n      \\\"description\\\": \\\"boca\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"der Teint (-s)\\\",\\n      \\\"description\\\": \\\"tez\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"der Pickel (-)\\\",\\n      \\\"description\\\": \\\"grano\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"der Leberfleck (-e)\\\",\\n      \\\"description\\\": \\\"lunar\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"die Sommersprosse (-n)\\\",\\n      \\\"description\\\": \\\"peca\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"die Falte (-n)\\\",\\n      \\\"description\\\": \\\"arruga\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"das Grübchen (-)\\\",\\n      \\\"description\\\": \\\"hoyuelo\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"die Brille (-n)\\\",\\n      \\\"description\\\": \\\"gafas\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"jung\\\",\\n      \\\"description\\\": \\\"joven\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"alt\\\",\\n      \\\"description\\\": \\\"viejo\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"groß\\\",\\n      \\\"description\\\": \\\"alto\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"klein\\\",\\n      \\\"description\\\": \\\"bajo\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"mittelgroß\\\",\\n      \\\"description\\\": \\\"de estatura media\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"dick\\\",\\n      \\\"description\\\": \\\"gordo\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"fettleibig\\\",\\n      \\\"description\\\": \\\"obeso\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"dünn\\\",\\n      \\\"description\\\": \\\"flaco, delgado\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"schlank\\\",\\n      \\\"description\\\": \\\"esbelto\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"muskulös\\\",\\n      \\\"description\\\": \\\"musculoso\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"gebrechlich\\\",\\n      \\\"description\\\": \\\"frágil\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"schön\\\",\\n      \\\"description\\\": \\\"guapo\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"gut aussehend\\\",\\n      \\\"description\\\": \\\"bien parecido\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"hübsch\\\",\\n      \\\"description\\\": \\\"bonito\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"hässlich\\\",\\n      \\\"description\\\": \\\"feo\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"pickelig\\\",\\n      \\\"description\\\": \\\"lleno de granos\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"sonnengebräunt\\\",\\n      \\\"description\\\": \\\"moreno\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"braun gebrannt\\\",\\n      \\\"description\\\": \\\"bronceado\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"blass\\\",\\n      \\\"description\\\": \\\"pálido\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"faltig\\\",\\n      \\\"description\\\": \\\"arrugado\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"er/sie hat... Augen\\\",\\n      \\\"description\\\": \\\"él/ella tiene los ojos…\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"blaue\\\",\\n      \\\"description\\\": \\\"azules\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"grüne\\\",\\n      \\\"description\\\": \\\"verdes\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"graue\\\",\\n      \\\"description\\\": \\\"grises\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"braune\\\",\\n      \\\"description\\\": \\\"marrones\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"hellbraune\\\",\\n      \\\"description\\\": \\\"marrón claro\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"dunkelbraune\\\",\\n      \\\"description\\\": \\\"marrón oscuro\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"schwarze\\\",\\n      \\\"description\\\": \\\"negros\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"graublaue\\\",\\n      \\\"description\\\": \\\"gris azulado\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"graugrüne\\\",\\n      \\\"description\\\": \\\"gris verde\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"wie ist er? können Sie ihn beschreiben?\\\",\\n      \\\"description\\\": \\\"¿cómo es (él)? ¿Puede describirlo?\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"ich bin 1 (ein) Meter 75 groß ich wiege 70 Kilo\\\",\\n      \\\"description\\\": \\\"mido 1 metro 75 peso 70 kilos\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"ein Mann mittlerer Größe\\\",\\n      \\\"description\\\": \\\"un hombre de mediana estatura\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"er benimmt sich eigenartig er sieht etwas seltsam aus\\\",\\n      \\\"description\\\": \\\"se comporta de un modo extraño tiene una pinta un poco rara\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"er hat ein Doppelkinn sie hat eine Stupsnase\\\",\\n      \\\"description\\\": \\\"tiene papada tiene la nariz respingona\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"sie hat eine krumme/spitze/gerade Nase\\\",\\n      \\\"description\\\": \\\"tiene la nariz aguileña/puntiaguda/recta\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"von Kopf bis Fuß o vom Kopf bis zu den Füßen\\\",\\n      \\\"description\\\": \\\"de pies a cabeza o de la cabeza a los pies\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    },\\n    {\\n      \\\"title\\\": \\\"die Brille\\\",\\n      \\\"description\\\": \\\"gafas (singular femenino en alemán)\\\",\\n      \\\"rate\\\": 1.0,\\n      \\\"probability\\\": 0.0\\n    }\\n  ]\\n}\\n```\\n\\n### Explanation:\\n- **generalDescription**: Provides a summary of the document's purpose and structure.\\n- **cardsList**: Contains individual vocabulary words and phrases from the text, each with its translation or meaning. Each object in the list has a `title` (the German word or phrase), a `description` (the translation or explanation), and default values for `rate` (1.0) and `probability` (0.0).\\n- The text focuses on vocabulary related to describing people and clothing, so the extracted concepts are organized accordingly.\\n\\nLet me know if you need further adjustments or additional sections!\"},\"logprobs\":null,\"finish_reason\":\"stop\"}],\"usage\":{\"prompt_tokens\":5641,\"completion_tokens\":3175,\"total_tokens\":8816,\"prompt_tokens_details\":{\"cached_tokens\":5632},\"prompt_cache_hit_tokens\":5632,\"prompt_cache_miss_tokens\":9},\"system_fingerprint\":\"fp_3a5770e1b4_prod0225\"}";

//            System.out.println("RESPONSE: " + responseBody);

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

                var newAutomaticDeckDto = objectMapper.readValue(jsonText, new TypeReference<NewAutomaticDeckDto>() {
                });

                if (!newAutomaticDeckDto.getCardsList().isEmpty()) {
                    double totalRate = newAutomaticDeckDto.getCardsList().stream()
                            .reduce(0.0, (sum, card) -> sum + card.getRate(), Double::sum);

                    var cardsWithProbability = newAutomaticDeckDto.getCardsList().stream().peek(card -> card.setProbability(card.getRate() / totalRate)).toList();

                    newAutomaticDeckDto.setCardsList(cardsWithProbability);
                }

                return newAutomaticDeckDto;
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

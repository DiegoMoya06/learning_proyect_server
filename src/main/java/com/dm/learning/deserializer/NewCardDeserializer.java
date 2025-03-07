package com.dm.learning.deserializer;

import com.dm.learning.dto.card.NewCardDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class NewCardDeserializer extends JsonDeserializer<NewCardDto> {
    @Override
    public NewCardDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        NewCardDto cardDto = new NewCardDto();
        cardDto.setTitle(node.get("title").asText());
        cardDto.setDescription(node.get("description").asText());
        cardDto.setRate(node.get("rate").asDouble());
        cardDto.setProbability(node.get("probability").asDouble());

        cardDto.setTimesDisplayed(0);
        cardDto.setLastDisplayed(null);

        return cardDto;
    }
}

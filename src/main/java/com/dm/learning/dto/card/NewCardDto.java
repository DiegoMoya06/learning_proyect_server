package com.dm.learning.dto.card;

import com.dm.learning.deserializer.NewCardDeserializer;
import com.dm.learning.entities.Card;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonDeserialize(using = NewCardDeserializer.class)
public class NewCardDto {

    private UUID id;
    private String title;
    private String description;
    private UUID deckId;
    private Double rate;
    private Integer timesDisplayed;
    private Instant lastDisplayed;
    private Double probability;

    private NewCardDto(@NonNull Card entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.description = entity.getDescription();
        this.deckId = entity.getDeck().getId();
        this.rate = entity.getRate();
        this.timesDisplayed = entity.getTimesDisplayed();
        this.lastDisplayed = entity.getLastDisplayed();
        this.probability = entity.getProbability();
    }

    public static NewCardDto fromEntity(Card entity) {
        if (entity == null) {
            return null;
        }

        return new NewCardDto(entity);
    }

    private static Stream<NewCardDto> fromEntities(Collection<Card> entities) {
        if (entities == null) {
            return Stream.empty();
        }

        return entities.stream().map(NewCardDto::fromEntity);
    }

    public static List<NewCardDto> fromEntitiesAsList(Collection<Card> entities) {
        return fromEntities(entities).toList();
    }

    public Double getRate() {
        return rate;
    }
}

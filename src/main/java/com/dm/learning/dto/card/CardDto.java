package com.dm.learning.dto.card;

import com.dm.learning.dto.base.BaseDto;
import com.dm.learning.entities.Card;
import lombok.*;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class CardDto extends BaseDto {

    private UUID id;
    private String title;
    private String description;
    private UUID deckId;
    private Double rate;
    private Integer timesDisplayed;
    private Instant lastDisplayed;
    private Double probability;

    private CardDto(@NonNull Card entity) {
        super(entity);
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.description = entity.getDescription();
        this.deckId = entity.getDeck().getId();
        this.rate = entity.getRate();
        this.timesDisplayed = entity.getTimesDisplayed();
        this.lastDisplayed = entity.getLastDisplayed();
        this.probability = entity.getProbability();
    }

    public static CardDto fromEntity(Card entity) {
        if (entity == null) {
            return null;
        }

        return new CardDto(entity);
    }

    private static Stream<CardDto> fromEntities(Collection<Card> entities) {
        if (entities == null) {
            return Stream.empty();
        }

        return entities.stream().map(CardDto::fromEntity);
    }

    public static List<CardDto> fromEntitiesAsList(Collection<Card> entities) {
        Comparator<CardDto> cardNameComparator = Comparator.comparing(CardDto::getTitle);
        var formattedList = new java.util.ArrayList<>(fromEntities(entities).toList());
        formattedList.sort(cardNameComparator);

        return formattedList;
    }
}

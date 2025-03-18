package com.dm.learning.dto.deck;

import com.dm.learning.dto.base.BaseDto;
import com.dm.learning.dto.card.CardDto;
import com.dm.learning.entities.Deck;
import lombok.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class DeckDto extends BaseDto {

    private UUID id;
    private String name;
    private String description;
    private String type;
    private List<CardDto> cards;

    private DeckDto(@NonNull Deck entity) {
        super(entity);
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.type = entity.getType().getId().toString();
        this.cards = CardDto.fromEntitiesAsList(entity.getCards());
    }

    public static DeckDto fromEntity(Deck entity) {
        if (entity == null) {
            return null;
        }

        return new DeckDto(entity);
    }

    private static Stream<DeckDto> fromEntities(Collection<Deck> entities) {
        if (entities == null) {
            return Stream.empty();
        }

        return entities.stream().map(DeckDto::fromEntity);
    }

    public static List<DeckDto> fromEntitiesAsList(Collection<Deck> entities) {
        return fromEntities(entities).toList();
    }

    public static Set<DeckDto> fromEntitiesAsSet(Collection<Deck> entities) {
        return fromEntities(entities).collect(Collectors.toSet());
    }
}

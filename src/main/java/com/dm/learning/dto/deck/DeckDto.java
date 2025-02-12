package com.dm.learning.dto.deck;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@EqualsAndHashCode(callSuper = true)
//public class DeckDto extends BaseDto {
public class DeckDto {

    private UUID id;
    private String name;
    private String description;
    private String type;

//    private DeckDto(@NonNull Deck entity) {
//        super(entity);
//        this.id = entity.getId();
//        this.name = entity.getName();
//        this.description = entity.getDescription();
//        this.type = entity.getType().getId().toString();
//    }
//
//    public static DeckDto fromEntity(Deck entity) {
//        if (entity == null) {
//            return null;
//        }
//
//        return new DeckDto(entity);
//    }
//
//    private static Stream<DeckDto> fromEntities(Collection<Deck> entities) {
//        if (entities == null) {
//            return Stream.empty();
//        }
//
//        return entities.stream().map(DeckDto::fromEntity);
//    }
//
//    public static List<DeckDto> fromEntitiesAsList(Collection<Deck> entities) {
//        return fromEntities(entities).toList();
//    }
//
//    public static Set<DeckDto> fromEntitiesAsSet(Collection<Deck> entities) {
//        return fromEntities(entities).collect(Collectors.toSet());
//    }
}

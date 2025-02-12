package com.dm.learning.dto.deck;

import com.dm.learning.dto.base.BaseDto;
import com.dm.learning.entities.Deck;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class DeckDto  extends BaseDto {

    private UUID id;
    private String name;
    private String description;
    private String type;

    private DeckDto(@NonNull Deck entity) {
        super(entity);
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.type = entity.getType().getId().toString();
    }
}

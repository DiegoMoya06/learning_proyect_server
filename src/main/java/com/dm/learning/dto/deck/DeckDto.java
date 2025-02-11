package com.dm.learning.dto.deck;

import com.dm.learning.dto.base.BaseDto;
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
}

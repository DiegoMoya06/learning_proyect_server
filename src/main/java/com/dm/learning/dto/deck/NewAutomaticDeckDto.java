package com.dm.learning.dto.deck;

import com.dm.learning.dto.card.NewCardDto;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewAutomaticDeckDto {
    private UUID id;
    private String generalDescription;
    private List<NewCardDto> cardsList;
}

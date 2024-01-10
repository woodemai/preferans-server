package com.ru.preferans.entities.move;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoveDto {

    private String id;
    private String playerId;
    private String cardId;
    private String roundId;
}

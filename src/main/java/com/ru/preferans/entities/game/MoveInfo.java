package com.ru.preferans.entities.game;

import com.ru.preferans.entities.card.CardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class MoveInfo {

    private CardDto card;
    private String playerId;

}

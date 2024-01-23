package com.ru.preferans.entities.game;

import com.ru.preferans.entities.card.CardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameDto {

    private UUID id;
    private GameState state;
    private short size;
    private List<CardDto> cards;
    private short currentPlayerIndex;
}

package com.ru.preferans.entities.game;

import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.card.CardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameDto {

    private UUID id;
    private GameState state;
    private short size;
    private Set<Card> purchase;
    private Set<Card> tableDeck;
    private short currentPlayerIndex;
}

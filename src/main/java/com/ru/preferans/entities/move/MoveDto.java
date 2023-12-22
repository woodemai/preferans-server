package com.ru.preferans.entities.move;

import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.player.Player;
import com.ru.preferans.entities.round.Round;
import jakarta.persistence.OneToMany;
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

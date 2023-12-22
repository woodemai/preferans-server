package com.ru.preferans.entities.move;

import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.player.Player;
import com.ru.preferans.entities.round.Round;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Move {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Player player;

    @ManyToOne
    private Card card;

    @ManyToOne
    private Round round;
}
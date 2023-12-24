package com.ru.preferans.entities.player;

import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.move.Move;
import com.ru.preferans.entities.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private int score;

    @OneToOne
    private User user;

    @ManyToMany
    private List<Game> games;

    @OneToMany
    private List<Move> moves;

    @ManyToMany
    private List<Card> cards;
}

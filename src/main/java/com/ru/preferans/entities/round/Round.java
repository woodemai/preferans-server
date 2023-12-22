package com.ru.preferans.entities.round;

import com.ru.preferans.entities.bet.Bet;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.move.Move;
import com.ru.preferans.entities.table.Table;
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
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private int number;
    private RoundState state;

    @ManyToOne
    private Game game;

    @ManyToOne
    private Table table;

    @OneToMany
    private List<Move> moves;

    @OneToMany
    private List<Bet> bets;
}

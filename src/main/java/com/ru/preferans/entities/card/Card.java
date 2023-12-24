package com.ru.preferans.entities.card;

import com.ru.preferans.entities.move.Move;
import com.ru.preferans.entities.player.Player;
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
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Suit suit;
    private Rank rank;

    @OneToMany
    private List<Move> moves;

    @ManyToMany
    private List<Player> players;

    @ManyToOne
    private Table table;
}

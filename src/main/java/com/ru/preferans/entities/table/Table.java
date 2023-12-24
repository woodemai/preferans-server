package com.ru.preferans.entities.table;

import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.round.Round;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private int maxPlayersQuantity;
    private int currentPlayersQuantity;
    private TableState state;

    @OneToMany
    private List<Round> rounds;

    @OneToMany
    private List<Card> cards;

    @OneToOne
    private Game game;

    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedDate
    private Timestamp updatedAt;
}

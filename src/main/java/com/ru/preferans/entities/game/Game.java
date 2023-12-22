package com.ru.preferans.entities.game;

import com.ru.preferans.entities.player.Player;
import com.ru.preferans.entities.round.Round;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private LocalDateTime startedTime;
    private LocalDateTime endedTime;

    @ManyToMany
    private List<Player> players;

    @OneToMany
    private List<Round> rounds;
}

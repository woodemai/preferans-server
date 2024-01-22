package com.ru.preferans.entities.card;

import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Suit suit;

    @Enumerated(EnumType.STRING)
    private Rank rank;

    @ToString.Exclude
    @ManyToMany
    private Set<Game> games;

    @ToString.Exclude
    @ManyToMany
    private Set<User> players;
}

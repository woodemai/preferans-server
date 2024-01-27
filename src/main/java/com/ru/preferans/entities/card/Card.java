package com.ru.preferans.entities.card;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
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
}

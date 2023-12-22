package com.ru.preferans.entities.bet;

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
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private BetType type;
    private int value;

    @ManyToOne
    private Round round;
}

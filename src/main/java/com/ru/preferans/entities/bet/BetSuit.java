package com.ru.preferans.entities.bet;

import lombok.Getter;

@Getter
public enum BetSuit {
    SPADES(1),
    CLUBS(2),
    DIAMONDS(3),
    HEARTS(4),
    NT(5);
    private final int value;

    BetSuit(int value) {
        this.value = value;
    }

}

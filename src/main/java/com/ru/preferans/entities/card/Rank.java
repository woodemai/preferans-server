package com.ru.preferans.entities.card;

import lombok.Getter;

@Getter
public enum Rank {
   SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);
   private final int value;

    Rank(int value) {
        this.value = value;
    }

}

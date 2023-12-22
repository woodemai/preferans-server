package com.ru.preferans.entities.bet;

import lombok.Getter;

@Getter
public enum BetType {
    CONTRACT("Контракт"),
    PASS("Пас"),
    MIZER("Мизер"),
    OPEN_MIZER("Открытый мизер"),
    NO_TRUMP("Без козырей"),
    SLAM("Слэм"),
    SLAM_OPEN("Слэм открытый"),
    SLAM_DOUBLE("Слэм двойной"),
    SLAM_DOUBLE_OPEN("Слэм двойной открытый");

    private final String displayName;

    BetType(String displayName) {
        this.displayName = displayName;
    }

}
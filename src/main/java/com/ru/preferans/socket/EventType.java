package com.ru.preferans.socket;

import lombok.Getter;

@Getter
public enum EventType {
    INFO("info"),
    HANDLE_READY("handle_ready"),
    ALL_READY("all_ready"),
    NEXT_TURN("next_turn"),
    MOVE_PURCHASE("move_purchase"),
    HANDLE_STATE("handle_state"),
    MOVE("move"),
    BRIBE_END("bribe_end");
    private final String type;

    EventType(String type) {
        this.type = type;
    }
}

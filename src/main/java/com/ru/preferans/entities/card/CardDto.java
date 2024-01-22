package com.ru.preferans.entities.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CardDto {
    private UUID id;
    private Rank rank;
    private Suit suit;
}

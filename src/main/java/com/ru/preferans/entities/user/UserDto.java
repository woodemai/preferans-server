package com.ru.preferans.entities.user;

import com.ru.preferans.entities.card.Card;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private UUID id;
    private String email;
    private String name;
    private int score;
    private boolean ready;
    private Set<Card> cards;
}

package com.ru.preferans.entities.user;

import com.ru.preferans.entities.card.CardDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String id;
    private String email;
    private String name;
    private int score;
    private boolean ready;
    private String role;
    private List<CardDto> cards;
}

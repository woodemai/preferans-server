package com.ru.preferans.entities.user;

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
    private String password;
    private String name;
    private int score;
    private boolean ready;
    private String gameId;
    private List<String> moveIds;
    private List<String> cardIds;
    private String role;
    private String playerId;
}

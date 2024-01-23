package com.ru.preferans.entities.game;

import com.ru.preferans.entities.user.UserDto;
import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class GameInfo {

    private GameDto game;
    private List<UserDto> users;
}

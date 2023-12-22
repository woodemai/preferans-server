package com.ru.preferans.entities.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerDto {

    private String id;
    private String name;
    private int score;
    private String userId;
    private List<String> gameIds;
    private List<String> moveIds;
}

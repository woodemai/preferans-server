package com.ru.preferans.entities.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameDto {

    private String id;
    private String state;
    private List<String> playerIds;
    private List<String> roundIds;
    private String tableId;

    private String createdAt;
    private String updatedAt;
}

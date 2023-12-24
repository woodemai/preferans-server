package com.ru.preferans.entities.table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableDto {
    private String id;
    private int maxPlayersQuantity;
    private int currentPlayersQuantity;
    private String state;

    private List<String> roundIds;
    private String gameId;

    private String createdAt;
    private String updatedAt;
}

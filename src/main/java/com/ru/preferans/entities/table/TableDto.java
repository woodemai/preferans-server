package com.ru.preferans.entities.table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableDto {
    private String id;
    private int maxPlayersQuantity;
    private int currentPlayersQuantity;
    private String state;
}

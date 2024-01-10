package com.ru.preferans.entities.round;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoundDto {

    private String id;
    private int number;
    private String state;
    private String gameId;
    private String tableId;
    private List<String> moveIds;
    private List<String> betIds;
}

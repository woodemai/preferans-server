package com.ru.preferans.entities.bet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BetDto {

    private String id;

    private String type;
    private int value;
    private String roundId;
}

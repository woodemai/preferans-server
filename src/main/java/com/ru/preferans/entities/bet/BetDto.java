package com.ru.preferans.entities.bet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BetDto {

    private String id;

    private String type;
    private int value;
    private String roundId;

    private String createdAt;
    private String updatedAt;
}

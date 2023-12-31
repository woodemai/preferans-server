package com.ru.preferans.entities.card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDto {

    private String id;

    private String suit;
    private String rank;
    private List<String> moveIds;
    private List<String> players;

    private String createdAt;
    private String updatedAt;
}

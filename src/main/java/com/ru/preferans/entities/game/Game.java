package com.ru.preferans.entities.game;

import com.ru.preferans.entities.round.Round;
import com.ru.preferans.entities.table.Table;
import com.ru.preferans.entities.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private GameState state;

    @ManyToMany
    private List<User> players;

    @OneToMany
    private List<Round> rounds;

    @OneToOne
    private Table table;

    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedDate
    private Timestamp updatedAt;
}

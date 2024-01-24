package com.ru.preferans.repositories;

import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.game.GameState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<Game, UUID>, JpaSpecificationExecutor<Game> {
    @Transactional
    @Modifying
    @Query("update Game g set g.state = ?1 where g.id = ?2")
    void updateStateById(GameState state, UUID id);

    @Transactional
    @Modifying
    @Query("update Game g set g.state = ?1, g.currentPlayerIndex = ?2 where g.id = ?3")
    void updateStateAndCurrentPlayerIndexById(GameState state, short currentPlayerIndex, UUID id);

    @Query("select (count(g) > 0) from Game g where g.id = ?1 and g.currentPlayerIndex = ?2")
    boolean existsByIdAndCurrentPlayerIndex(UUID id, short currentPlayerIndex);



    @Transactional
    @Modifying
    @Query("update Game g set g.state = ?1, g.currentPlayerIndex = ?2 where g.id = ?3")
    void updateStateAndCurrentPlayerIndexById(GameState state, short currentPlayerIndex, UUID id);

    @Query("select (count(g) > 0) from Game g where g.id = ?1 and g.currentPlayerIndex = ?2")
    boolean existsByIdAndCurrentPlayerIndex(UUID id, short currentPlayerIndex);

}
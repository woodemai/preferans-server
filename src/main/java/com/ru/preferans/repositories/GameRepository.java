package com.ru.preferans.repositories;

import com.ru.preferans.entities.game.Game;
import org.springframework.data.domain.Pageable;
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
    @Query("delete from Game g where g.players is empty and g.id = ?1")
    void deleteByPlayersEmptyAndId(UUID id);


}
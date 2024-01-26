package com.ru.preferans.repositories;

import com.ru.preferans.entities.bet.Bet;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    @Transactional
    @Modifying
    @Query("update User u set u.ready = FALSE, u.game = null where u.id = ?1")
    void updateReadyAndGameById(UUID id);

    @Transactional
    @Modifying
    @Query("update User u set u.ready = false, u.game = null, u.bet = null where u.id = ?1")
    void reset(UUID id);


    Optional<User> getByEmail(String email);

    List<User> findByGame_Id(UUID id);

    @Query("select u from User u where u.game.id = ?1 order by u.email")
    List<User> findByGame_IdOrderByEmailAsc(UUID id);


    @Transactional
    @Modifying
    @Query("update User u set u.game = ?1 where u.id = ?2")
    void updateGameById(Game game, UUID id);

    @Query("select count(u) from User u where u.game.id = ?1")
    short countByGame_Id(UUID id);

    @Query("select (count(u) > 0) from User u where u.email = ?1")
    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("update User u set u.bet = ?1 where u.id = ?2")
    void updateBetById(Bet bet, UUID id);

    @Query("select (count(u) > 0) from User u where u.game.id = ?1 and u.bet is null")
    boolean existsByGame_IdAndBetNull(UUID id);

}


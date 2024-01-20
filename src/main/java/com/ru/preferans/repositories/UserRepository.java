package com.ru.preferans.repositories;

import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Transactional
    @Modifying
    @Query("update User u set u.ready = FALSE, u.game = null where u.id = ?1")
    void updateReadyAndGameById(String id);

    Optional<User> getByEmail(String email);

    List<User> findByGame_Id(String id);

    @Transactional
    @Modifying
    @Query("update User u set u.game = ?1 where u.id = ?2")
    void updateGameById(Game game, String id);

    @Query("select count(u) from User u where u.game.id = ?1")
    long countByGame_Id(String id);


}

package com.ru.preferans.repositories;

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
    @Query("update User u set u.ready = false, u.game = null, u.bet = null where u.id = ?1")
    void reset(UUID id);


    Optional<User> getByEmail(String email);

    List<User> findByGame_Id(UUID id);

    @Query("select u from User u where u.game.id = ?1 order by u.email")
    List<User> findByGame_IdOrderByEmailAsc(UUID id);


    @Query("select (count(u) > 0) from User u where u.email = ?1")
    boolean existsByEmail(String email);

}


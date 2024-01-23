package com.ru.preferans.repositories;



import com.ru.preferans.entities.token.Token;
import com.ru.preferans.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID>, JpaSpecificationExecutor<Token> {

    @Query("select t from Token t where t.refreshToken = ?1")
    Optional<Token> findByRefreshToken(String refreshToken);


    Optional<Token> getByUser(User user);

    @Query("select (count(t) > 0) from Token t where t.user = ?1")
    boolean existsByUser(User user);

    @Transactional
    @Modifying
    @Query("update Token t set t.refreshToken = ?1 where t.user = ?2")
    void updateRefreshTokenByUser(String refreshToken, User user);

    @Transactional
    @Modifying
    @Query("delete from Token t where t.refreshToken = ?1")
    int deleteByRefreshToken(String refreshToken);


}

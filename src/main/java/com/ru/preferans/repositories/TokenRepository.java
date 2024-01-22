package com.ru.preferans.repositories;



import com.ru.preferans.entities.token.Token;
import com.ru.preferans.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {

    @Query("select t from Token t where t.refreshToken = ?1")
    Optional<Token> findByRefreshToken(String refreshToken);


    Optional<Token> getByUser(User user);
}

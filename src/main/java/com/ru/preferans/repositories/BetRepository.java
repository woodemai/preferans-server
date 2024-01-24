package com.ru.preferans.repositories;

import com.ru.preferans.entities.bet.Bet;
import com.ru.preferans.entities.bet.BetSuit;
import com.ru.preferans.entities.bet.BetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BetRepository extends JpaRepository<Bet, UUID>, JpaSpecificationExecutor<Bet> {
    @Query("select b from Bet b where b.type = ?1 and b.suit = ?2 and b.value = ?3")
    Optional<Bet> findByTypeAndSuitAndValue(@Nullable BetType type, @Nullable BetSuit suit, @Nullable int value);

}
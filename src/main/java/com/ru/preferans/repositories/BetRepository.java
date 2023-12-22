package com.ru.preferans.repositories;

import com.ru.preferans.entities.bet.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetRepository extends JpaRepository<Bet, String> {
}

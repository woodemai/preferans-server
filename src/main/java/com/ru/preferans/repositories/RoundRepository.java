package com.ru.preferans.repositories;

import com.ru.preferans.entities.round.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundRepository extends JpaRepository<Round, String> {
}

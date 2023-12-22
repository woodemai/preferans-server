package com.ru.preferans.repositories;

import com.ru.preferans.entities.move.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveRepository extends JpaRepository<Move, String> {
}

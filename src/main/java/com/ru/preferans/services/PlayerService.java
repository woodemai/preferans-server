package com.ru.preferans.services;

import com.ru.preferans.entities.player.Player;
import com.ru.preferans.repositories.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository repository;

    public Player getPlayer(String id) {
        return repository.findById(id)
                .orElseThrow(() -> playerNotFound(id));
    }

    private EntityNotFoundException playerNotFound(String id) {
        return new EntityNotFoundException("Player with id " + id + " was not found");
    }
}

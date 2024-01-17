package com.ru.preferans.controllers;

import com.ru.preferans.entities.user.UserDto;
import com.ru.preferans.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GameSocketController {

    private final PlayerService playerService;

    @CrossOrigin
    @GetMapping("/{gameId}")
    public ResponseEntity<List<UserDto>> getPlayers(@PathVariable String gameId) {
        return ResponseEntity.ok(playerService.getPlayers(gameId));
    }
}

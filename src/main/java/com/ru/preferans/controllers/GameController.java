package com.ru.preferans.controllers;

import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.game.GameDto;
import com.ru.preferans.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/game")
public class GameController {

    private final GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<GameDto> createGame() {
        Game game = gameService.create();
        GameDto dto = gameService.convertToDto(game);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GameDto>> getAll() {
        return ResponseEntity.ok(gameService.getAll().stream().map(gameService::convertToDto).toList());
    }
}

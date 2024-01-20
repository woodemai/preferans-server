package com.ru.preferans.controllers;

import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.game.GameDto;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserDto;
import com.ru.preferans.services.GameService;
import com.ru.preferans.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/game")
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<GameDto> createGame() {
        Game game = gameService.create();
        GameDto dto = gameService.convertToDto(game);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/start")
    public ResponseEntity<GameDto> startGame(@RequestParam String gameId) {
        Game game = gameService.start(gameId);
        GameDto dto = gameService.convertToDto(game);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/end")
    public ResponseEntity<GameDto> endGame(@RequestParam String gameId) {
        Game game = gameService.end(gameId);
        GameDto dto = gameService.convertToDto(game);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GameDto>> getAll() {
        return ResponseEntity.ok(gameService.getAll().stream().map(gameService::convertToDto).toList());
    }

    @GetMapping("/players")
    public ResponseEntity<List<UserDto>> getPlayers(@RequestParam String gameId) {
        List<User> players = userService.getByGame(gameId);
        List<UserDto> playerDtos = userService.convertListToDto(players);
        return ResponseEntity.ok(playerDtos);
    }

    @PutMapping("/ready")
    public ResponseEntity<UserDto> switchReady(@RequestParam String playerId) {
        return ResponseEntity.ok(userService.convertToDto(userService.switchReady(playerId)));
    }

    @GetMapping("/one")
    public ResponseEntity<GameDto> getOne(@RequestParam String gameId) {
        return ResponseEntity.ok(gameService.convertToDto(gameService.getById(gameId)));
    }
}

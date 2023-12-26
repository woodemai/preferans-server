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
    public ResponseEntity<GameDto> createGame(@RequestParam String playerId) {
        User player = userService.getById(playerId);
        Game game = gameService.createGame(player);
        player.setGame(game);
        userService.save(player);
        GameDto dto = gameService.convertToDto(game);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/connect")
    public ResponseEntity<GameDto> connectPlayer(@RequestParam String playerId, @RequestParam String gameId) {
        User player = userService.getById(playerId);
        Game game = gameService.getGame(gameId);
        game = gameService.connectPlayer(player, game);
        return ResponseEntity.ok(gameService.convertToDto(game));
    }

    @PostMapping("/disconnect")
    public ResponseEntity<GameDto> disconnectPlayer(@RequestParam String playerId, @RequestParam String gameId) {
        User player = userService.getById(playerId);
        Game game = gameService.getGame(gameId);
        game = gameService.disconnectPlayer(player, game);
        if(game == null) {
            return null;
        }
        GameDto dto = gameService.convertToDto(game);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/start")
    public ResponseEntity<GameDto> startGame(@RequestParam String gameId) {
        Game game = gameService.startGame(gameId);
        GameDto dto = gameService.convertToDto(game);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/end")
    public ResponseEntity<GameDto> endGame(@RequestParam String gameId) {
        Game game = gameService.endGame(gameId);
        GameDto dto = gameService.convertToDto(game);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/all")
    public ResponseEntity<List<GameDto>> getAll() {
        return ResponseEntity.ok(gameService.getAll().stream().map(gameService::convertToDto).toList());
    }
    @GetMapping("/players")
    public ResponseEntity<List<UserDto>> getPlayers(@RequestParam String gameId) {
        return ResponseEntity.ok(gameService.getGame(gameId).getPlayers().stream().map(userService::convertToDto).toList());
    }
    @PutMapping("/ready")
    public ResponseEntity<UserDto> switchReady(@RequestParam String playerId) {
        return ResponseEntity.ok(userService.convertToDto(userService.switchReady(playerId)));
    }
}

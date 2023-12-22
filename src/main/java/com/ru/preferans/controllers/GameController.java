package com.ru.preferans.controllers;

import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.game.GameDto;
import com.ru.preferans.entities.player.Player;
import com.ru.preferans.services.GameService;
import com.ru.preferans.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/game")
public class GameController {

    private final GameService gameService;
    private final PlayerService playerService;

    @PostMapping("/create")
    public ResponseEntity<GameDto> createGame(@RequestParam String playerId) {
        Player player = playerService.getPlayer(playerId);
        Game game = gameService.createGame(player);
        GameDto dto = gameService.convertToDto(game);
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/connect")
    public ResponseEntity<GameDto> connectPlayer(@RequestParam String playerId, @RequestParam String gameId) {
        Player player = playerService.getPlayer(playerId);
        Game game = gameService.connectPlayer(player, gameId);
        GameDto dto = gameService.convertToDto(game);
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/disconnect")
    public ResponseEntity<GameDto> disconnectPlayer(@RequestParam String playerId, @RequestParam String gameId) {
        Player player = playerService.getPlayer(playerId);
        Game game = gameService.disconnectPlayer(player, gameId);
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
}

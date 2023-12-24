package com.ru.preferans.controllers;

import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.game.GameDto;
import com.ru.preferans.entities.table.Table;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.services.CardService;
import com.ru.preferans.services.GameService;
import com.ru.preferans.services.TableService;
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
    private final TableService tableService;
    private final CardService cardService;

    @PostMapping("/create")
    public ResponseEntity<GameDto> createGame(@RequestParam String playerId) {
        List<Card> cards = cardService.createShuffledDeck();
        User player = userService.getById(playerId);
        Table table = tableService.create(cards.subList(0, 2));
        Game game = gameService.createGame(player, table);
        tableService.setGame(table.getId(), game);
        GameDto dto = gameService.convertToDto(game);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/connect")
    public ResponseEntity<GameDto> connectPlayer(@RequestParam String playerId, @RequestParam String gameId) {
        User player = userService.getById(playerId);
        Game game = gameService.connectPlayer(player, gameId);
        GameDto dto = gameService.convertToDto(game);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/disconnect")
    public ResponseEntity<GameDto> disconnectPlayer(@RequestParam String playerId, @RequestParam String gameId) {
        User player = userService.getById(playerId);
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
    @GetMapping("/all")
    public ResponseEntity<List<GameDto>> getAll() {
        return ResponseEntity.ok(gameService.getAll().stream().map(gameService::convertToDto).toList());
    }
}

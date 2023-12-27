package com.ru.preferans.controllers;

import com.ru.preferans.entities.card.Card;
import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.game.GameDto;
import com.ru.preferans.entities.game.GameState;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserDto;
import com.ru.preferans.services.CardService;
import com.ru.preferans.services.GameService;
import com.ru.preferans.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/game")
public class GameController {

    private final GameService gameService;
    private final UserService userService;
    private final CardService cardService;
    private final SimpMessagingTemplate messagingTemplate;


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

    @MessageMapping("/change-game")
    @SendTo("/topic/game")
    public ResponseEntity<GameDto> processGame (@Payload GameDto dto) {
        boolean isAllReady = userService.checkAllReady(dto.getPlayerIds());
        Game game = gameService.getGame(dto.getId());
        if(isAllReady) {
            game.setState(GameState.STARTED);
        }
        if(game.getState() == GameState.STARTED) {
            List<Card> cards = cardService.createShuffledDeck();
            gameService.startGame(game, cards);
        }
        return ResponseEntity.ok(gameService.convertToDto(game));
    }
}

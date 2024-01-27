package com.ru.preferans.controllers;

import com.ru.preferans.entities.game.Game;
import com.ru.preferans.entities.game.GameDto;
import com.ru.preferans.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<GameDto>> getAll(@RequestParam int pageNumber,@RequestParam int pageSize) {
        return ResponseEntity.ok(gameService.getGames(pageNumber, pageSize, Sort.by(Sort.Order.desc("players"))).stream().map(gameService::convertToDto).toList());
    }
}

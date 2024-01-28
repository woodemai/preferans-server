package com.ru.preferans.controllers;

import com.ru.preferans.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @PatchMapping("/change-name")
    public ResponseEntity<Void> changeName(@RequestParam UUID userId, @RequestParam String newName  ) {
        userService.handleNameChanging(userId, newName);
        return ResponseEntity.noContent().build();
    }

}

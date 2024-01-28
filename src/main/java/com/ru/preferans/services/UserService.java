package com.ru.preferans.services;


import com.ru.preferans.entities.EntityType;
import com.ru.preferans.entities.auth.RegisterRequest;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserRole;
import com.ru.preferans.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final ErrorService errorService;

    public User getByEmail(String email) {
        return repository.getByEmail(email)
                .orElseThrow(() -> errorService.getEntityNotFoundException(EntityType.USER,email));
    }

    public User save(RegisterRequest request) {
        if (!repository.existsByEmail(request.getEmail())) {
            return repository.save(User.builder()
                    .email(request.getEmail())
                    .password(encoder.encode(request.getPassword()))
                    .name(request.getName())
                    .role(UserRole.USER)
                    .build());
        } else {
            throw errorService.getEntityExistException(EntityType.USER, request.getEmail());
        }
    }

    public void handleNameChanging(UUID userId, String newName) {
        repository.updateName(newName, userId);
    }
}
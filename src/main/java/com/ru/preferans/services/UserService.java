package com.ru.preferans.services;


import com.ru.preferans.entities.auth.RegisterRequest;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserRole;
import com.ru.preferans.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public User getUser(String email) {
        return repository.getByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + "not found"));
    }

    public User setUser(RegisterRequest request) {
        Optional<User> optUser = repository.getByEmail(request.getEmail());
        if (optUser.isPresent()) {
            throw new EntityExistsException("User with email: " + request.getEmail() + " already exists");
        }
        var user = User.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .name(request.getName())
                .role(UserRole.USER)
                .build();
        return repository.save(user);
    }

    public User getById(String playerId) {
        return repository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player with id " + playerId + "not found"));
    }
}
package com.ru.preferans.services;


import com.ru.preferans.entities.auth.RegisterRequest;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserDto;
import com.ru.preferans.entities.user.UserRole;
import com.ru.preferans.repositories.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
                .score(0)
                .ready(false)
                .role(UserRole.USER)
                .build();
        return repository.save(user);
    }

    public User getById(String playerId) {
        return repository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player with id " + playerId + "not found"));
    }

    public User save(User player) {
        return repository.save(player);
    }

    public UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .score(user.getScore())
                .ready(user.isReady())
                .role(user.getRole().toString())
                .build();
    }
    public List<UserDto> convertListToDto(List<User> users) {
        return users.stream().map(this::convertToDto).toList();
    }

    public User switchReady(String playerId) {
        User user = getById(playerId);
        user.setReady(!user.isReady());
        return save(user);
    }

    public List<User> getByGame(String gameId) {
        return repository.findByGame_Id(gameId);
    }
}
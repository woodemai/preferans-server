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

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String NOT_FOUND_MESSAGE = "User with ID '%s' not found";
    private static final String EXISTS_MESSAGE = "User with email '%s' already exists";

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public User getByEmail(String email) {
        return repository.getByEmail(email)
                .orElseThrow(() -> getNotFoundExc(email));
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
            throw getExistsExc(request.getEmail());
        }
    }

    private EntityNotFoundException getNotFoundExc(String email) {
        return new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, email));
    }

    private EntityExistsException getExistsExc(String email) {
        return new EntityExistsException(String.format(EXISTS_MESSAGE, email));
    }

}
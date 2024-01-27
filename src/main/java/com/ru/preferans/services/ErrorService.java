package com.ru.preferans.services;

import com.ru.preferans.entities.EntityType;
import com.ru.preferans.config.Constants;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ErrorService {

    public EntityExistsException getEntityExistException(EntityType type, Object id) {
        return new EntityExistsException(String.format(Constants.ENTITY_ALREADY_EXIST_EXC, type, id));
    }
    public EntityNotFoundException getEntityNotFoundException(EntityType type, Object id) {
        return new EntityNotFoundException(String.format(Constants.ENTITY_NOT_FOUND_EXC, type, id));
    }
    public AuthenticationServiceException getAuthenticationServiceException(String message) {
        return new AuthenticationServiceException(message);
    }
}

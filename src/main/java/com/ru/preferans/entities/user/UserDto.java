package com.ru.preferans.entities.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String id;
    private String email;
    private String password;
    private String name;
    private String role;
    private String playerId;

    private String createdAt;
    private String updatedAt;
}

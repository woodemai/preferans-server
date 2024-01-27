package com.ru.preferans.entities.auth;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest extends AuthenticationRequest {

    private String email;
    private String password;
    private String name;
}
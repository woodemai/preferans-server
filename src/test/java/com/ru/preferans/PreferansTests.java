package com.ru.preferans;

import com.ru.preferans.entities.auth.AuthenticationRequest;
import com.ru.preferans.entities.auth.AuthenticationResponse;
import com.ru.preferans.entities.auth.RegisterRequest;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PreferansTests {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void registrationShouldPass() {
        String name = "Name";
        String email = "n.savchenkoo73@gmail.com";
        String password = "password";
        ResponseEntity<AuthenticationResponse> response = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/register",
                new RegisterRequest(
                        email,
                        password,
                        name
                ), AuthenticationResponse.class);
        UserDto user = Objects.requireNonNull(response.getBody()).getUser();
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getCards()).isNull();


        ResponseEntity<AuthenticationResponse> responseLogin = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/auth/login",
                new AuthenticationRequest(email, password), AuthenticationResponse.class);
        UserDto userLogin = Objects.requireNonNull(responseLogin.getBody()).getUser();
        assertThat(userLogin.getName()).isEqualTo(name);
        assertThat(userLogin.getEmail()).isEqualTo(email);
        assertThat(userLogin.getCards()).isNull();
    }
}

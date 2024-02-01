package com.ru.preferans.services;


import com.ru.preferans.entities.auth.AuthenticationRequest;
import com.ru.preferans.entities.auth.AuthenticationResponse;
import com.ru.preferans.entities.auth.RegisterRequest;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.entities.user.UserDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    public AuthenticationResponse registration(RegisterRequest request, HttpServletResponse httpServletResponse) {
        User user = userService.save(request);
        login(request);
        String refreshToken = tokenService.generateRefreshToken(user);
        String accessToken = tokenService.generateAccessToken(user);
        tokenService.save(refreshToken, user);
        setTokenToCookie(httpServletResponse, refreshToken);
        return buildResponse(accessToken, user);
    }

    public AuthenticationResponse login(AuthenticationRequest request, HttpServletResponse httpServletResponse) {
        User user = userService.getByEmail(request.getEmail());
        login(request);
        String refreshToken = tokenService.generateRefreshToken(user);
        String accessToken = tokenService.generateAccessToken(user);
        tokenService.save(refreshToken, user);
        setTokenToCookie(httpServletResponse, refreshToken);
        return buildResponse(accessToken, user);
    }

    public AuthenticationResponse refresh(String requestToken, HttpServletResponse httpServletResponse) {
        tokenService.checkEquals(requestToken);
        String email = tokenService.getEmail(requestToken);
        User user = userService.getByEmail(email);
        String refreshToken = tokenService.generateAccessToken(user);
        String accessToken = tokenService.generateRefreshToken(user);
        tokenService.save(refreshToken, user);
        setTokenToCookie(httpServletResponse, refreshToken);
        return buildResponse(accessToken, user);
    }

    public void logout(String refreshToken) {
        tokenService.deleteToken(refreshToken);

    }


    private void setTokenToCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    private AuthenticationResponse buildResponse(String accessToken, User user) {
        var dto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .ready(user.isReady())
                .score(user.getScore())
                .build();
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .user(dto)
                .build();
    }

    private <T extends AuthenticationRequest> void login(T request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
    }
}

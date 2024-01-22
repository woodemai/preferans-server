package com.ru.preferans.services;

import com.ru.preferans.entities.token.Token;
import com.ru.preferans.entities.user.User;
import com.ru.preferans.repositories.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TokenService {
    private static final String SECRET_KEY = "PuRYMpxdC09jCkjRDyY5FdGUU1yBEPoBmHQ+hIq+K61Fq5eO+kI2W2ehW63WngaeucauZ4GjgxbsS1lQz7qY/gAmoN0VUE+/DMc1A/3H2ia1HTZwuXy74ZrmKTft0mXBW58HT9clbaPNTlhpt6wEJFaXHHUX1GzM+cVhwoOoG0cXE+heEBhbM+YU0/grYSZPQE3j7ZolRBeBS8p/V3ZPaPjxQ/vSdZUMEmDHLi3Z1KJk0BC+iZL3wtZYJWMig8cYBczE2xsVGewUXC8rXzqq23NwWeqAsur04oDhxVLwmzu/LVX2jDkGnl6QbPeXDT+btDpM5iveDs9QCSQHOPmn6MQwd1iI74ruD0OXU97lJ0hr52MC6f+NKcQJKK30FQWz0Yjrmmx3wjJM9tIHL7csDrzqFQUYo44da8qgTraAoafZu05tDC0IM9lnq04zwDoTMs3M8HJw2cbwKucfiM1B/N4xw2Zu1KsfXr2/8hAW1RGGQHfiGvguqOwdTHcVxxVcPPk4jjUjjNUARFr1EYXMrpjeHbrtTUCePd16UKxJODjusG1HBe+rVN2GMZhzd0AFFUy6Gzk2j1n7QPtGOZ+O2C8fP6fRp2kpIjAbu5ngRiASJyimcQHNkPJSCBG35xZtUPI7Ltz6YcqNQaLv5i9uIBEoXK6EeemPERl5xkzNimV8mhlVTF9B60czeMH6FhGy";
    private final TokenRepository repository;

    private EntityNotFoundException getNotFound() {
        return new EntityNotFoundException("Token not found");
    }

    private String generateToken(UserDetails userDetails, long expiration) {
        return generateToken(new HashMap<>(), userDetails, expiration);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, 1000 * 60 * 24 * 30L);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, 1000 * 60 * 60 * 24 * 15L);
    }


    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder().claims()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .add(extraClaims)
                .and()
                .signWith(getSignInKey())
                .compact();

    }

    public SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public void checkEquals(String token) {
        Token dbToken = repository.findByRefreshToken(token)
                .orElseThrow(this::getNotFound);
        if (!dbToken.getRefreshToken().equals(token)) {
            throw new EntityExistsException("Tokens are not equal");
        }
    }

    public void setTokenToRepository(String refreshToken, User user) {
        Optional<Token> tokenOpt = repository.getByUser(user);
        if (tokenOpt.isPresent()) {
            Token token = tokenOpt.get();
            token.setRefreshToken(refreshToken);
            repository.save(token);
        } else {
            repository.save(new Token(user, refreshToken));
        }
    }

    public void deleteToken(String token) {
        repository.delete(repository.findByRefreshToken(token)
                .orElseThrow(this::getNotFound));
    }
}
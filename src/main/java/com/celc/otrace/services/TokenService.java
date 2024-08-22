package com.celc.otrace.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.celc.otrace.domain.User.Account;

@Service
public class TokenService {
    @Value("${api.security.secret}")
    private String secret;

    static private final String ISSUER = "OTrace";

    public String genToken(Account account) {
        try {
            return JWT.create()
            .withIssuer(ISSUER)
            .withSubject(account.getEmail())
            .withExpiresAt(expirationDate())
            .sign(algorithm());
        } catch (JWTCreationException e) {
            e.printStackTrace();
            throw new RuntimeException("Create token Error");
        }

    }

    public Algorithm algorithm() {
        return Algorithm.HMAC256(secret);
    }

    public Instant expirationDate() {
        return LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSubject(String token) {
        return JWT.decode(token).getSubject();
    }
}

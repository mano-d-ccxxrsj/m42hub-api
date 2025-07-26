package com.m42hub.m42hub_api.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.m42hub.m42hub_api.user.entity.User;
import org.flywaydb.core.internal.license.FlywayJWTValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class TokenService {

    @Value("${m42hub.security.secret")
    private String secret;

    public String generateToken(User user) {

        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("id", user.getId())
                .withClaim("role", user.getSystemRole().getName())
                .withExpiresAt(Instant.now().plusSeconds(28800)) //8 horas
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUserData> verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);

            return Optional.of(JWTUserData.builder()
                    .username(jwt.getSubject())
                    .id(jwt.getClaim("id").asLong())
                    .role(jwt.getClaim("role").asString())
                    .build());

        } catch (FlywayJWTValidationException exception) {
            return Optional.empty();
        }
    }

}

package dev.sivalabs.geeksclub.users.domain;

import static java.time.temporal.ChronoUnit.DAYS;

import dev.sivalabs.geeksclub.ApplicationProperties;
import dev.sivalabs.geeksclub.users.domain.dto.AuthToken;
import dev.sivalabs.geeksclub.users.domain.dto.UserVM;
import java.time.Instant;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenProvider {
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final ApplicationProperties properties;

    TokenProvider(JwtEncoder encoder, JwtDecoder decoder, ApplicationProperties properties) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.properties = properties;
    }

    public AuthToken generate(UserVM user) {
        Instant now = Instant.now();
        Instant accessTokenExpiresAt = now.plusSeconds(properties.jwt().expiresInSeconds());
        Instant refreshTokenExpiresAt = now.plus(7, DAYS);
        var accessToken = generateToken(user, now, accessTokenExpiresAt);
        var refreshToken = generateToken(user, now, refreshTokenExpiresAt);
        return new AuthToken(accessToken, accessTokenExpiresAt, refreshToken, refreshTokenExpiresAt);
    }

    private String generateToken(UserVM user, Instant now, Instant expiresAt) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.jwt().issuer())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(user.email())
                .claim("user_id", user.id())
                .claim("roles", user.role().name())
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Jwt validateToken(String token) {
        return this.decoder.decode(token);
    }
}

package dev.sivalabs.geeksclub.users.domain.dto;

import java.time.Instant;

public record AuthToken(
        String accessToken, Instant accessTokenExpiresAt, String refreshToken, Instant refreshTokenExpiresAt) {}

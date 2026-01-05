package dev.sivalabs.geeksclub.users.domain.dto;

import java.time.Instant;

public record LoginResult(
        String accessToken,
        Instant accessTokenExpiresAt,
        String refreshToken,
        Instant refreshTokenExpiresAt,
        String fullName,
        String username,
        String email,
        String role) {}

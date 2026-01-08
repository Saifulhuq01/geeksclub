package dev.sivalabs.geeksclub.rest.dto;

import java.time.Instant;

public record LoginResponse(
        String accessToken,
        Instant accessTokenExpiresAt,
        String refreshToken,
        Instant refreshTokenExpiresAt,
        String fullName,
        String username,
        String email,
        String role) {}

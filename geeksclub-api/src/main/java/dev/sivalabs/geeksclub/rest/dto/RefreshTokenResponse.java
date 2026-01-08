package dev.sivalabs.geeksclub.rest.dto;

import java.time.Instant;

public record RefreshTokenResponse(
        String accessToken, Instant accessTokenExpiresAt, String refreshToken, Instant refreshTokenExpiresAt) {}

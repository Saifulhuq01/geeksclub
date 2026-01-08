package dev.sivalabs.geeksclub.domain.dto;

import java.time.Instant;

public record ActiveUserVM(
        Long userId,
        String fullName,
        String username,
        String email,
        long messageCount,
        long voteCount,
        long totalActivity,
        Instant lastActivityAt,
        Instant joinedAt) {}

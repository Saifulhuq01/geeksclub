package dev.sivalabs.geeksclub.rest.dto;

import java.time.Instant;
import java.util.List;

public record MostActiveUsersResponse(List<ActiveUser> users, int limit) {
    public record ActiveUser(
            Long userId,
            String fullName,
            String username,
            String email,
            long messageCount,
            long voteCount,
            long totalActivity,
            Instant lastActivityAt,
            Instant joinedAt) {}
}

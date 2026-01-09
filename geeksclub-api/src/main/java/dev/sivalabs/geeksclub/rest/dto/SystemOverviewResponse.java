package dev.sivalabs.geeksclub.rest.dto;

import java.time.Instant;

public record SystemOverviewResponse(
        long totalUsers,
        long activeUsersLast7Days,
        long totalMessages,
        long messagesLast24Hours,
        long totalVotes,
        long votesLast24Hours,
        long spamDetected,
        double spamRate,
        Instant timestamp) {}

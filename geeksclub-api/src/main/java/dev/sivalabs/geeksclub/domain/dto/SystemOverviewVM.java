package dev.sivalabs.geeksclub.domain.dto;

import java.time.Instant;

public record SystemOverviewVM(
        long totalUsers,
        long activeUsersLast7Days,
        long totalMessages,
        long messagesLast24Hours,
        long totalVotes,
        long votesLast24Hours,
        long spamDetected,
        double spamRate,
        Instant timestamp) {}

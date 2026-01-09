package dev.sivalabs.geeksclub.domain.dto;

import java.util.List;

public record SpamStatisticsVM(
        long totalMessages,
        long spamDetected,
        double spamRate,
        double averageConfidence,
        List<SpamStatByDateVM> byDate,
        List<FlaggedMessageVM> flaggedMessages) {}

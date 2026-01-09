package dev.sivalabs.geeksclub.rest.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record SpamStatisticsResponse(
        long totalMessages,
        long spamDetected,
        double spamRate,
        double averageConfidence,
        List<SpamStatByDate> byDate,
        List<FlaggedMessage> flaggedMessages) {
    public record SpamStatByDate(LocalDate date, long totalMessages, long spamCount, double spamRate) {}

    public record FlaggedMessage(Long id, String content, double spamConfidence, String status, Instant createdAt) {}
}

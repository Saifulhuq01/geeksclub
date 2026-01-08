package dev.sivalabs.geeksclub.rest.dto;

import java.time.LocalDate;
import java.util.List;

public record DailyStatisticsResponse(List<DailyStatistic> statistics, Period period) {
    public record DailyStatistic(LocalDate date, int messageCount, int activeUsers, int spamCount, int totalVotes) {}

    public record Period(LocalDate startDate, LocalDate endDate, int days) {}
}

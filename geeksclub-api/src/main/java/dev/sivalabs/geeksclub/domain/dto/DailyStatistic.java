package dev.sivalabs.geeksclub.domain.dto;

import java.time.LocalDate;

public record DailyStatistic(LocalDate date, int messageCount, int activeUsers, int spamCount, int totalVotes) {}

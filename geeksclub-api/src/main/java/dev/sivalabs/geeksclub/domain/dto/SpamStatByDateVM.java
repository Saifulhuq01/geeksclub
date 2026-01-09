package dev.sivalabs.geeksclub.domain.dto;

import java.time.LocalDate;

public record SpamStatByDateVM(LocalDate date, long totalMessages, long spamCount, double spamRate) {}

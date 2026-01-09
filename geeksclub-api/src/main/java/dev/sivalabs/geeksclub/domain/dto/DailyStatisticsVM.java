package dev.sivalabs.geeksclub.domain.dto;

import java.time.LocalDate;
import java.util.List;

public record DailyStatisticsVM(List<DailyStatistic> statistics, Period period) {
    public record Period(LocalDate startDate, LocalDate endDate, int days) {}
}

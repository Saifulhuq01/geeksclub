package dev.sivalabs.geeksclub.domain.dto;

import java.time.LocalDate;
import java.util.List;

public record TrendingMessagesVM(List<TrendingMessageVM> messages, Period period) {
    public record Period(int days, LocalDate startDate) {}
}

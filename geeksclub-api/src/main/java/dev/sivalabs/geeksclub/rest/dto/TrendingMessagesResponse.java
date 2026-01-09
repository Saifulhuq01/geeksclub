package dev.sivalabs.geeksclub.rest.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record TrendingMessagesResponse(List<TrendingMessage> messages, Period period) {
    public record TrendingMessage(
            Long id,
            String content,
            AuthorInfo author,
            long upvoteCount,
            long downvoteCount,
            long score,
            long recentVotes,
            Instant createdAt) {}

    public record AuthorInfo(Long id, String fullName, String username) {}

    public record Period(int days, LocalDate startDate) {}
}

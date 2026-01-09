package dev.sivalabs.geeksclub.domain.dto;

import java.time.Instant;

public record TrendingMessageVM(
        Long id,
        String content,
        AuthorInfo author,
        long upvoteCount,
        long downvoteCount,
        long score,
        long recentVotes,
        Instant createdAt) {
    public record AuthorInfo(Long id, String fullName, String username) {}
}

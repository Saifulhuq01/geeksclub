package dev.sivalabs.geeksclub.rest.dto;

import dev.sivalabs.geeksclub.domain.dto.MessageStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record MessageDetailResponse(
        Long id,
        String content,
        AuthorInfo author,
        MessageStatus status,
        boolean isSpam,
        BigDecimal spamConfidence,
        VotesInfo votes,
        String userVote,
        Instant createdAt,
        Instant updatedAt) {

    public record AuthorInfo(Long id, String fullName, String username) {}

    public record VotesInfo(int upvoteCount, int downvoteCount, int score) {}
}

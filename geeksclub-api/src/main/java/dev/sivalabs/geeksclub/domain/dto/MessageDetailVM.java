package dev.sivalabs.geeksclub.domain.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record MessageDetailVM(
        Long id,
        String content,
        Long authorId,
        String authorUsername,
        String authorFullName,
        MessageStatus status,
        boolean isSpam,
        BigDecimal spamConfidence,
        long upvoteCount,
        long downvoteCount,
        long score,
        String userVote,
        Instant createdAt,
        Instant updatedAt) {}

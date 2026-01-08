package dev.sivalabs.geeksclub.messages.domain.dto;

import dev.sivalabs.geeksclub.messages.domain.MessageStatus;
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
        int upvoteCount,
        int downvoteCount,
        int score,
        String userVote,
        Instant createdAt,
        Instant updatedAt) {}

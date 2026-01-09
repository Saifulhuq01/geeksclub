package dev.sivalabs.geeksclub.domain.dto;

import java.time.Instant;

public record MessageFeedItemVM(
        Long id,
        String content,
        Long authorId,
        String authorFullName,
        String authorUsername,
        MessageStatus status,
        boolean isSpam,
        int upvoteCount,
        int downvoteCount,
        int score,
        String userVote,
        Instant createdAt) {}

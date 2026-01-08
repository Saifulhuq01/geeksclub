package dev.sivalabs.geeksclub.messages.domain.dto;

import dev.sivalabs.geeksclub.messages.domain.MessageStatus;
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

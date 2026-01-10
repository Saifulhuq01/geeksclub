package dev.sivalabs.geeksclub.rest.dto;

import dev.sivalabs.geeksclub.domain.dto.MessageStatus;
import java.time.Instant;

public record MessageFeedItem(
        Long id,
        String content,
        AuthorInfo author,
        MessageStatus status,
        boolean isSpam,
        VotesInfo votes,
        String userVote,
        Instant createdAt) {

    public record AuthorInfo(Long id, String fullName, String username) {}

    public record VotesInfo(long upvoteCount, long downvoteCount, long score) {}
}

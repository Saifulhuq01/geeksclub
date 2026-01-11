package dev.sivalabs.geeksclub.domain.dto;

import java.time.Instant;

public record VoteResult(
        Long messageId, VoteType voteType, long upvoteCount, long downvoteCount, long score, Instant votedAt) {}

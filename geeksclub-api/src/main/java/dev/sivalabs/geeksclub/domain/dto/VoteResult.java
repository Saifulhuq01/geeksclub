package dev.sivalabs.geeksclub.domain.dto;

import java.time.Instant;

public record VoteResult(
        Long messageId, VoteType voteType, int upvoteCount, int downvoteCount, int score, Instant votedAt) {}

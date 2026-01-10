package dev.sivalabs.geeksclub.rest.dto;

import dev.sivalabs.geeksclub.domain.dto.VoteType;
import java.time.Instant;

public record VoteResponse(Long messageId, VoteType voteType, Votes votes, Instant votedAt) {
    public record Votes(long upvoteCount, long downvoteCount, long score) {}
}

package dev.sivalabs.geeksclub.domain.dto;

import java.time.Instant;

public record UserVoteResult(Long messageId, VoteType voteType, Instant votedAt) {}

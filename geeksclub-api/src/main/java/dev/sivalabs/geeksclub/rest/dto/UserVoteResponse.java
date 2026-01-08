package dev.sivalabs.geeksclub.rest.dto;

import dev.sivalabs.geeksclub.domain.dto.VoteType;
import java.time.Instant;

public record UserVoteResponse(Long messageId, VoteType voteType, Instant votedAt) {}

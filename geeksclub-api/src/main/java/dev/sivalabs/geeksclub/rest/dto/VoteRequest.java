package dev.sivalabs.geeksclub.rest.dto;

import dev.sivalabs.geeksclub.domain.dto.VoteType;
import jakarta.validation.constraints.NotNull;

public record VoteRequest(@NotNull VoteType voteType) {}

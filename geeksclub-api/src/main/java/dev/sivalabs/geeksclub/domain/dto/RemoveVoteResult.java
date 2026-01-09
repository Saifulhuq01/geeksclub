package dev.sivalabs.geeksclub.domain.dto;

public record RemoveVoteResult(Long messageId, int upvoteCount, int downvoteCount, int score, String message) {}

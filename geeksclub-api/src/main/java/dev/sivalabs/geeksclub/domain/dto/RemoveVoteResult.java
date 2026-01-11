package dev.sivalabs.geeksclub.domain.dto;

public record RemoveVoteResult(Long messageId, long upvoteCount, long downvoteCount, long score, String message) {}

package dev.sivalabs.geeksclub.rest.dto;

public record RemoveVoteResponse(Long messageId, VoteResponse.Votes votes, String message) {}

package dev.sivalabs.geeksclub.domain.dto;

import java.time.Instant;

public record FlaggedMessageVM(
        Long id, String content, double spamConfidence, MessageStatus status, Instant createdAt) {}

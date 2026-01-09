package dev.sivalabs.geeksclub.domain.dto;

import java.time.Instant;

public record ReviewMessageResult(Long id, MessageStatus status, String reviewedBy, Instant reviewedAt, String notes) {}

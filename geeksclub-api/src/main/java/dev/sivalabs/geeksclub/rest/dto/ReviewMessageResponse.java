package dev.sivalabs.geeksclub.rest.dto;

import java.time.Instant;

public record ReviewMessageResponse(Long id, String status, String reviewedBy, Instant reviewedAt, String notes) {}

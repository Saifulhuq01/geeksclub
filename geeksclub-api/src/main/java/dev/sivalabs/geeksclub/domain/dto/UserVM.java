package dev.sivalabs.geeksclub.domain.dto;

import java.time.Instant;

public record UserVM(
        Long id, String fullName, String username, String email, String password, Role role, Instant createdAt) {}

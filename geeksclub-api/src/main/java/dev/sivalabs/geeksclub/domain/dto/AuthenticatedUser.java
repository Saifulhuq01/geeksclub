package dev.sivalabs.geeksclub.domain.dto;

public record AuthenticatedUser(Long id, String email, String fullName, String username, Role role) {}

package dev.sivalabs.geeksclub.users.domain.dto;

import dev.sivalabs.geeksclub.users.domain.Role;

public record AuthenticatedUser(Long id, String email, String fullName, String username, Role role) {}

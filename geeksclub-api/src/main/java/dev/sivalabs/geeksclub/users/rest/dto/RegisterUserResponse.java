package dev.sivalabs.geeksclub.users.rest.dto;

import dev.sivalabs.geeksclub.users.domain.Role;

public record RegisterUserResponse(String fullName, String username, String email, Role role) {}

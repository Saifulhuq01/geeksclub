package dev.sivalabs.geeksclub.rest.dto;

import dev.sivalabs.geeksclub.domain.dto.Role;

public record RegisterUserResponse(String fullName, String username, String email, Role role) {}

package dev.sivalabs.geeksclub.users.domain.dto;

import dev.sivalabs.geeksclub.users.domain.Role;

public record UserVM(Long id, String fullName, String username, String email, String password, Role role) {}

package dev.sivalabs.geeksclub.users.domain;

public record UserVM(Long id, String fullName, String username, String email, String password, Role role) {}

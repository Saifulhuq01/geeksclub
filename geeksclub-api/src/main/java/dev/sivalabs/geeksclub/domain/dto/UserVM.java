package dev.sivalabs.geeksclub.domain.dto;

public record UserVM(Long id, String fullName, String username, String email, String password, Role role) {}

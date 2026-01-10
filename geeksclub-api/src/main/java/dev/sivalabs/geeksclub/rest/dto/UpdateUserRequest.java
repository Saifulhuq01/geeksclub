package dev.sivalabs.geeksclub.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank(message = "Full name must not be empty")
        @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
        @Pattern(regexp = "^[a-zA-Z\\s'-]+$", message = "Full name contains invalid characters")
        String fullName) {}

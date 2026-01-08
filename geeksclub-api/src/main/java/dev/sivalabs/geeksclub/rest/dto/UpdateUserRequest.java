package dev.sivalabs.geeksclub.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @NotBlank(message = "FullName is required")
        @Size(min = 4, max = 20, message = "FullName must be between 4 and 20 characters")
        String fullName) {}

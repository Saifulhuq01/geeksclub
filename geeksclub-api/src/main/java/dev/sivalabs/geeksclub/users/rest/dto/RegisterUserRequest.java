package dev.sivalabs.geeksclub.users.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank(message = "FullName is required")
        @Size(min = 4, max = 20, message = "FullName must be between 4 and 20 characters")
        String fullName,

        @NotBlank(message = "Username is required")
        @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
        String username,

        @NotBlank(message = "Email is required") @Email(message = "Invalid email address")
        String email,

        @NotBlank(message = "Password is required") String password) {}

package dev.sivalabs.geeksclub.messages.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateMessageRequest(
        @NotBlank(message = "Content must not be empty")
        @Size(min = 1, max = 5000, message = "Content must not exceed 5000 characters")
        String content) {}

package dev.sivalabs.geeksclub.rest.dto;

import jakarta.validation.constraints.NotNull;

public record ReviewMessageRequest(@NotNull ReviewAction action, String notes) {
    public enum ReviewAction {
        APPROVE,
        REMOVE
    }
}

package dev.sivalabs.geeksclub.messages.domain.dto;

import dev.sivalabs.geeksclub.messages.domain.MessageStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record MessageVM(
        Long id,
        Long userId,
        String content,
        MessageStatus status,
        boolean isSpam,
        BigDecimal spamConfidence,
        Instant createdAt,
        Instant updatedAt) {}

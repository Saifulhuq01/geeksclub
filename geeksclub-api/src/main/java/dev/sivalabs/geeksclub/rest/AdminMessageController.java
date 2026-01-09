package dev.sivalabs.geeksclub.rest;

import dev.sivalabs.geeksclub.domain.dto.ReviewMessageResult;
import dev.sivalabs.geeksclub.domain.service.MessageService;
import dev.sivalabs.geeksclub.rest.dto.ReviewMessageRequest;
import dev.sivalabs.geeksclub.rest.dto.ReviewMessageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/messages")
@Tag(name = "Admin Message API")
class AdminMessageController {
    private final MessageService messageService;
    private final UserContextUtils userContextUtils;

    AdminMessageController(MessageService messageService, UserContextUtils userContextUtils) {
        this.messageService = messageService;
        this.userContextUtils = userContextUtils;
    }

    @PutMapping("/{id}/review")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<ReviewMessageResponse> reviewMessage(
            @PathVariable Long id, @Valid @RequestBody ReviewMessageRequest request) {
        String reviewedBy = userContextUtils.getCurrentUserOrThrow().username();

        ReviewMessageResult result =
                messageService.reviewMessage(id, request.action().name(), reviewedBy, request.notes());

        ReviewMessageResponse response = new ReviewMessageResponse(
                result.id(), result.status().name(), result.reviewedBy(), result.reviewedAt(), result.notes());

        return ResponseEntity.ok(response);
    }
}

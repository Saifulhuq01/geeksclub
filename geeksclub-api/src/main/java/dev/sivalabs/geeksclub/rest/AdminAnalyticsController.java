package dev.sivalabs.geeksclub.rest;

import dev.sivalabs.geeksclub.domain.dto.SystemOverviewVM;
import dev.sivalabs.geeksclub.domain.service.AnalyticsService;
import dev.sivalabs.geeksclub.rest.dto.SystemOverviewResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/analytics")
@Tag(name = "Admin Analytics API")
class AdminAnalyticsController {
    private final AnalyticsService analyticsService;

    AdminAnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/overview")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<SystemOverviewResponse> getSystemOverview() {
        SystemOverviewVM overview = analyticsService.getSystemOverview();

        SystemOverviewResponse response = new SystemOverviewResponse(
                overview.totalUsers(),
                overview.activeUsersLast7Days(),
                overview.totalMessages(),
                overview.messagesLast24Hours(),
                overview.totalVotes(),
                overview.votesLast24Hours(),
                overview.spamDetected(),
                overview.spamRate(),
                overview.timestamp());

        return ResponseEntity.ok(response);
    }
}

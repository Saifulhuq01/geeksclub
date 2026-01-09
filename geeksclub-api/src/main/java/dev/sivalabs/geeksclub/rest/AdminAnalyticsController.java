package dev.sivalabs.geeksclub.rest;

import dev.sivalabs.geeksclub.domain.dto.DailyStatisticsVM;
import dev.sivalabs.geeksclub.domain.dto.MostActiveUsersVM;
import dev.sivalabs.geeksclub.domain.dto.SpamStatisticsVM;
import dev.sivalabs.geeksclub.domain.dto.SystemOverviewVM;
import dev.sivalabs.geeksclub.domain.dto.TrendingMessagesVM;
import dev.sivalabs.geeksclub.domain.service.AnalyticsService;
import dev.sivalabs.geeksclub.rest.dto.DailyStatisticsResponse;
import dev.sivalabs.geeksclub.rest.dto.MostActiveUsersResponse;
import dev.sivalabs.geeksclub.rest.dto.SpamStatisticsResponse;
import dev.sivalabs.geeksclub.rest.dto.SystemOverviewResponse;
import dev.sivalabs.geeksclub.rest.dto.TrendingMessagesResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/daily")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<DailyStatisticsResponse> getDailyStatistics(@RequestParam(defaultValue = "30") int days) {
        DailyStatisticsVM stats = analyticsService.getDailyStatistics(days);

        var statistics = stats.statistics().stream()
                .map(s -> new DailyStatisticsResponse.DailyStatistic(
                        s.date(), s.messageCount(), s.activeUsers(), s.spamCount(), s.totalVotes()))
                .toList();

        var period = new DailyStatisticsResponse.Period(
                stats.period().startDate(),
                stats.period().endDate(),
                stats.period().days());

        DailyStatisticsResponse response = new DailyStatisticsResponse(statistics, period);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/active")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<MostActiveUsersResponse> getMostActiveUsers(@RequestParam(defaultValue = "20") int limit) {
        MostActiveUsersVM activeUsers = analyticsService.getMostActiveUsers(limit);

        var users = activeUsers.users().stream()
                .map(u -> new MostActiveUsersResponse.ActiveUser(
                        u.userId(),
                        u.fullName(),
                        u.username(),
                        u.email(),
                        u.messageCount(),
                        u.voteCount(),
                        u.totalActivity(),
                        u.lastActivityAt(),
                        u.joinedAt()))
                .toList();

        MostActiveUsersResponse response = new MostActiveUsersResponse(users, activeUsers.limit());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/messages/trending")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<TrendingMessagesResponse> getTrendingMessages(
            @RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue = "7") int days) {
        TrendingMessagesVM trending = analyticsService.getTrendingMessages(limit, days);

        var messages = trending.messages().stream()
                .map(m -> new TrendingMessagesResponse.TrendingMessage(
                        m.id(),
                        m.content(),
                        new TrendingMessagesResponse.AuthorInfo(
                                m.author().id(),
                                m.author().fullName(),
                                m.author().username()),
                        m.upvoteCount(),
                        m.downvoteCount(),
                        m.score(),
                        m.recentVotes(),
                        m.createdAt()))
                .toList();

        var period = new TrendingMessagesResponse.Period(
                trending.period().days(), trending.period().startDate());

        TrendingMessagesResponse response = new TrendingMessagesResponse(messages, period);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/spam")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAuthority('ADMIN')")
    ResponseEntity<SpamStatisticsResponse> getSpamStatistics(
            @RequestParam(defaultValue = "30") int days, @RequestParam(defaultValue = "10") int flaggedLimit) {
        SpamStatisticsVM stats = analyticsService.getSpamStatistics(days, flaggedLimit);

        var byDate = stats.byDate().stream()
                .map(s -> new SpamStatisticsResponse.SpamStatByDate(
                        s.date(), s.totalMessages(), s.spamCount(), s.spamRate()))
                .toList();

        var flaggedMessages = stats.flaggedMessages().stream()
                .map(m -> new SpamStatisticsResponse.FlaggedMessage(
                        m.id(), m.content(), m.spamConfidence(), m.status().name(), m.createdAt()))
                .toList();

        SpamStatisticsResponse response = new SpamStatisticsResponse(
                stats.totalMessages(),
                stats.spamDetected(),
                stats.spamRate(),
                stats.averageConfidence(),
                byDate,
                flaggedMessages);

        return ResponseEntity.ok(response);
    }
}

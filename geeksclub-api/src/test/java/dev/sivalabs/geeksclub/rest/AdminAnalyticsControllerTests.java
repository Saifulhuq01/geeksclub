package dev.sivalabs.geeksclub.rest;

import static org.assertj.core.api.Assertions.assertThat;

import dev.sivalabs.geeksclub.BaseIntegrationTest;
import dev.sivalabs.geeksclub.rest.dto.SystemOverviewResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
class AdminAnalyticsControllerTests extends BaseIntegrationTest {

    @Test
    void shouldGetSystemOverviewWhenAdmin() {
        String adminToken = getAdminAuthToken();

        SystemOverviewResponse response = restTestClient
                .get()
                .uri("/api/admin/analytics/overview")
                .header("Authorization", "Bearer " + adminToken)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(SystemOverviewResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.totalUsers()).isGreaterThan(0);
        assertThat(response.totalMessages()).isGreaterThan(0);
        assertThat(response.totalVotes()).isGreaterThanOrEqualTo(0);
        assertThat(response.spamDetected()).isGreaterThanOrEqualTo(0);
        assertThat(response.spamRate()).isGreaterThanOrEqualTo(0.0);
        assertThat(response.timestamp()).isNotNull();
    }

    @Test
    void shouldNotGetSystemOverviewWithoutAuthentication() {
        restTestClient
                .get()
                .uri("/api/admin/analytics/overview")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldNotGetSystemOverviewWhenNotAdmin() {
        String userToken = getUserAuthToken();

        restTestClient
                .get()
                .uri("/api/admin/analytics/overview")
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.FORBIDDEN);
    }
}

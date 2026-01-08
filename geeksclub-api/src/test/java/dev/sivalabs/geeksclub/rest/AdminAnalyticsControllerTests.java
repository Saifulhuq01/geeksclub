package dev.sivalabs.geeksclub.rest;

import static org.assertj.core.api.Assertions.assertThat;

import dev.sivalabs.geeksclub.BaseIntegrationTest;
import dev.sivalabs.geeksclub.rest.dto.DailyStatisticsResponse;
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

    @Test
    void shouldGetDailyStatisticsWhenAdmin() {
        String adminToken = getAdminAuthToken();

        DailyStatisticsResponse response = restTestClient
                .get()
                .uri("/api/admin/analytics/daily?days=7")
                .header("Authorization", "Bearer " + adminToken)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(DailyStatisticsResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.statistics()).isNotNull();
        assertThat(response.period()).isNotNull();
        assertThat(response.period().days()).isEqualTo(7);
        assertThat(response.period().startDate()).isNotNull();
        assertThat(response.period().endDate()).isNotNull();
    }

    @Test
    void shouldGetDailyStatisticsWithDefaultDaysWhenAdmin() {
        String adminToken = getAdminAuthToken();

        DailyStatisticsResponse response = restTestClient
                .get()
                .uri("/api/admin/analytics/daily")
                .header("Authorization", "Bearer " + adminToken)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(DailyStatisticsResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.statistics()).isNotNull();
        assertThat(response.period()).isNotNull();
        assertThat(response.period().days()).isEqualTo(30);
    }

    @Test
    void shouldNotGetDailyStatisticsWithoutAuthentication() {
        restTestClient
                .get()
                .uri("/api/admin/analytics/daily?days=7")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldNotGetDailyStatisticsWhenNotAdmin() {
        String userToken = getUserAuthToken();

        restTestClient
                .get()
                .uri("/api/admin/analytics/daily?days=7")
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.FORBIDDEN);
    }
}

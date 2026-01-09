package dev.sivalabs.geeksclub.rest;

import static org.assertj.core.api.Assertions.assertThat;

import dev.sivalabs.geeksclub.BaseIntegrationTest;
import dev.sivalabs.geeksclub.rest.dto.DailyStatisticsResponse;
import dev.sivalabs.geeksclub.rest.dto.MostActiveUsersResponse;
import dev.sivalabs.geeksclub.rest.dto.SystemOverviewResponse;
import dev.sivalabs.geeksclub.rest.dto.TrendingMessagesResponse;
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

    @Test
    void shouldGetMostActiveUsersWhenAdmin() {
        String adminToken = getAdminAuthToken();

        MostActiveUsersResponse response = restTestClient
                .get()
                .uri("/api/admin/analytics/users/active?limit=5")
                .header("Authorization", "Bearer " + adminToken)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(MostActiveUsersResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.users()).isNotNull();
        assertThat(response.users()).hasSizeLessThanOrEqualTo(5);
        assertThat(response.limit()).isEqualTo(5);

        if (!response.users().isEmpty()) {
            MostActiveUsersResponse.ActiveUser firstUser = response.users().get(0);
            assertThat(firstUser.userId()).isNotNull();
            assertThat(firstUser.username()).isNotBlank();
            assertThat(firstUser.email()).isNotBlank();
            assertThat(firstUser.totalActivity()).isGreaterThanOrEqualTo(0);
            assertThat(firstUser.messageCount()).isGreaterThanOrEqualTo(0);
            assertThat(firstUser.voteCount()).isGreaterThanOrEqualTo(0);
            assertThat(firstUser.lastActivityAt()).isNotNull();
            assertThat(firstUser.joinedAt()).isNotNull();
        }
    }

    @Test
    void shouldGetMostActiveUsersWithDefaultLimitWhenAdmin() {
        String adminToken = getAdminAuthToken();

        MostActiveUsersResponse response = restTestClient
                .get()
                .uri("/api/admin/analytics/users/active")
                .header("Authorization", "Bearer " + adminToken)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(MostActiveUsersResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.users()).isNotNull();
        assertThat(response.limit()).isEqualTo(20);
    }

    @Test
    void shouldNotGetMostActiveUsersWithoutAuthentication() {
        restTestClient
                .get()
                .uri("/api/admin/analytics/users/active?limit=5")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldNotGetMostActiveUsersWhenNotAdmin() {
        String userToken = getUserAuthToken();

        restTestClient
                .get()
                .uri("/api/admin/analytics/users/active?limit=5")
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldGetTrendingMessagesWhenAdmin() {
        String adminToken = getAdminAuthToken();

        TrendingMessagesResponse response = restTestClient
                .get()
                .uri("/api/admin/analytics/messages/trending?limit=5&days=7")
                .header("Authorization", "Bearer " + adminToken)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(TrendingMessagesResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.messages()).isNotNull();
        assertThat(response.messages()).hasSizeLessThanOrEqualTo(5);
        assertThat(response.period()).isNotNull();
        assertThat(response.period().days()).isEqualTo(7);
        assertThat(response.period().startDate()).isNotNull();

        if (!response.messages().isEmpty()) {
            TrendingMessagesResponse.TrendingMessage firstMessage =
                    response.messages().get(0);
            assertThat(firstMessage.id()).isNotNull();
            assertThat(firstMessage.content()).isNotBlank();
            assertThat(firstMessage.author()).isNotNull();
            assertThat(firstMessage.author().username()).isNotBlank();
            assertThat(firstMessage.upvoteCount()).isGreaterThanOrEqualTo(0);
            assertThat(firstMessage.downvoteCount()).isGreaterThanOrEqualTo(0);
            assertThat(firstMessage.recentVotes()).isGreaterThanOrEqualTo(0);
            assertThat(firstMessage.createdAt()).isNotNull();
        }
    }

    @Test
    void shouldGetTrendingMessagesWithDefaultParametersWhenAdmin() {
        String adminToken = getAdminAuthToken();

        TrendingMessagesResponse response = restTestClient
                .get()
                .uri("/api/admin/analytics/messages/trending")
                .header("Authorization", "Bearer " + adminToken)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(TrendingMessagesResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.messages()).isNotNull();
        assertThat(response.period()).isNotNull();
        assertThat(response.period().days()).isEqualTo(7);
    }

    @Test
    void shouldNotGetTrendingMessagesWithoutAuthentication() {
        restTestClient
                .get()
                .uri("/api/admin/analytics/messages/trending?limit=5&days=7")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldNotGetTrendingMessagesWhenNotAdmin() {
        String userToken = getUserAuthToken();

        restTestClient
                .get()
                .uri("/api/admin/analytics/messages/trending?limit=5&days=7")
                .header("Authorization", "Bearer " + userToken)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.FORBIDDEN);
    }
}

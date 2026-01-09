package dev.sivalabs.geeksclub.rest;

import static org.assertj.core.api.Assertions.assertThat;

import dev.sivalabs.geeksclub.BaseIntegrationTest;
import dev.sivalabs.geeksclub.rest.dto.ReviewMessageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
class AdminMessageControllerTests extends BaseIntegrationTest {

    @Test
    void shouldReviewMessageWithApproveActionWhenAdmin() {
        String adminToken = getAdminAuthToken();

        ReviewMessageResponse response = restTestClient
                .put()
                .uri("/api/admin/messages/1/review")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                            "action": "APPROVE",
                            "notes": "False positive - legitimate tech announcement"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(ReviewMessageResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.status()).isEqualTo("PUBLISHED");
        assertThat(response.reviewedBy()).isNotBlank();
        assertThat(response.reviewedAt()).isNotNull();
        assertThat(response.notes()).isEqualTo("False positive - legitimate tech announcement");
    }

    @Test
    void shouldReviewMessageWithRemoveActionWhenAdmin() {
        String adminToken = getAdminAuthToken();

        ReviewMessageResponse response = restTestClient
                .put()
                .uri("/api/admin/messages/1/review")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                            "action": "REMOVE",
                            "notes": "Confirmed spam"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(ReviewMessageResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.status()).isEqualTo("REMOVED");
        assertThat(response.reviewedBy()).isNotBlank();
        assertThat(response.reviewedAt()).isNotNull();
        assertThat(response.notes()).isEqualTo("Confirmed spam");
    }

    @Test
    void shouldReturnNotFoundWhenReviewingNonExistentMessage() {
        String adminToken = getAdminAuthToken();

        restTestClient
                .put()
                .uri("/api/admin/messages/999999/review")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                            "action": "APPROVE",
                            "notes": "Test notes"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotReviewMessageWithoutAuthentication() {
        restTestClient
                .put()
                .uri("/api/admin/messages/1/review")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                            "action": "APPROVE",
                            "notes": "Test notes"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldNotReviewMessageWhenNotAdmin() {
        String userToken = getUserAuthToken();

        restTestClient
                .put()
                .uri("/api/admin/messages/1/review")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                            "action": "APPROVE",
                            "notes": "Test notes"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldReturnBadRequestWhenActionIsNull() {
        String adminToken = getAdminAuthToken();

        restTestClient
                .put()
                .uri("/api/admin/messages/1/review")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                            "action": null,
                            "notes": "Test notes"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

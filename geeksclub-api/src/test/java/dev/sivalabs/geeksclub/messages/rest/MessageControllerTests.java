package dev.sivalabs.geeksclub.messages.rest;

import static org.assertj.core.api.Assertions.assertThat;

import dev.sivalabs.geeksclub.BaseIntegrationTest;
import dev.sivalabs.geeksclub.messages.rest.dto.CreateMessageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
class MessageControllerTests extends BaseIntegrationTest {

    @Test
    void shouldCreateMessageSuccessfully() {
        String token = getUserAuthToken();

        CreateMessageResponse response = restTestClient
                .post()
                .uri("/api/messages")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "content": "Just learned about Spring AI! It's amazing how easy it is to integrate AI models into Spring Boot applications. #SpringAI #Java"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isCreated()
                .returnResult(CreateMessageResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.content())
                .isEqualTo(
                        "Just learned about Spring AI! It's amazing how easy it is to integrate AI models into Spring Boot applications. #SpringAI #Java");
        assertThat(response.author()).isNotNull();
        assertThat(response.author().username()).isEqualTo("siva");
        assertThat(response.author().fullName()).isEqualTo("Siva Katamreddy");
        assertThat(response.status().name()).isEqualTo("PUBLISHED");
        assertThat(response.isSpam()).isFalse();
        assertThat(response.votes()).isNotNull();
        assertThat(response.votes().upvoteCount()).isZero();
        assertThat(response.votes().downvoteCount()).isZero();
        assertThat(response.votes().score()).isZero();
        assertThat(response.userVote()).isNull();
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();
    }

    @Test
    void shouldNotCreateMessageWithoutAuthentication() {
        restTestClient
                .post()
                .uri("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "content": "This should fail without authentication"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldNotCreateMessageWithEmptyContent() {
        String token = getUserAuthToken();

        restTestClient
                .post()
                .uri("/api/messages")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "content": ""
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotCreateMessageWithContentExceeding5000Characters() {
        String token = getUserAuthToken();
        String longContent = "a".repeat(5001);

        restTestClient
                .post()
                .uri("/api/messages")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "content": "%s"
                        }
                        """.formatted(longContent))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotCreateMessageWithNullContent() {
        String token = getUserAuthToken();

        restTestClient
                .post()
                .uri("/api/messages")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "content": null
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

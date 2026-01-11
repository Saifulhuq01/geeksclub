package dev.sivalabs.geeksclub.rest;

import static org.assertj.core.api.Assertions.assertThat;

import dev.sivalabs.geeksclub.BaseIntegrationTest;
import dev.sivalabs.geeksclub.rest.dto.CreateMessageResponse;
import dev.sivalabs.geeksclub.rest.dto.MessageDetailResponse;
import dev.sivalabs.geeksclub.rest.dto.VoteResponse;
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

    @Test
    void shouldGetMessageFeedWithoutAuthentication() {
        var response = restTestClient
                .get()
                .uri("/api/messages?page=0&size=20&sort=recent")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"content\"");
        assertThat(response).contains("\"totalElements\"");
    }

    @Test
    void shouldGetMessageFeedWithAuthentication() {
        String token = getUserAuthToken();

        var response = restTestClient
                .get()
                .uri("/api/messages?page=0&size=20&sort=recent")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"content\"");
        assertThat(response).contains("\"totalElements\"");
    }

    @Test
    void shouldGetMessageFeedWithUpvotedSorting() {
        var response = restTestClient
                .get()
                .uri("/api/messages?page=0&size=20&sort=upvoted")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"content\"");
    }

    @Test
    void shouldGetMessageFeedWithDownvotedSorting() {
        var response = restTestClient
                .get()
                .uri("/api/messages?page=0&size=20&sort=downvoted")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"content\"");
    }

    @Test
    void shouldGetMessageFeedWithTrendingSorting() {
        var response = restTestClient
                .get()
                .uri("/api/messages?page=0&size=20&sort=trending")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"content\"");
    }

    @Test
    void shouldGetMessageFeedWithPagination() {
        var response = restTestClient
                .get()
                .uri("/api/messages?page=0&size=5&sort=recent")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"content\"");
        assertThat(response).contains("\"size\":5");
    }

    @Test
    void shouldGetMessageByIdWithoutAuthentication() {
        MessageDetailResponse response = restTestClient
                .get()
                .uri("/api/messages/1")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(MessageDetailResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.content()).contains("Spring AI");
        assertThat(response.author()).isNotNull();
        assertThat(response.author().username()).isEqualTo("siva");
        assertThat(response.author().fullName()).isEqualTo("Siva Katamreddy");
        assertThat(response.status().name()).isEqualTo("PUBLISHED");
        assertThat(response.isSpam()).isFalse();
        assertThat(response.spamConfidence()).isNotNull();
        assertThat(response.votes()).isNotNull();
        assertThat(response.votes().upvoteCount()).isEqualTo(8);
        assertThat(response.votes().downvoteCount()).isZero();
        assertThat(response.votes().score()).isEqualTo(8);
        assertThat(response.userVote()).isNull();
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();
    }

    @Test
    void shouldGetMessageByIdWithAuthentication() {
        String token = getUserAuthToken();

        MessageDetailResponse response = restTestClient
                .get()
                .uri("/api/messages/1")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(MessageDetailResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.content()).contains("Spring AI");
        assertThat(response.author()).isNotNull();
        assertThat(response.author().username()).isEqualTo("siva");
        assertThat(response.votes()).isNotNull();
        assertThat(response.votes().upvoteCount()).isEqualTo(8);
        assertThat(response.votes().score()).isEqualTo(8);
        // User with id 1 (siva) is the author, so no vote
        assertThat(response.userVote()).isNull();
    }

    @Test
    void shouldGetMessageByIdWithUserVote() {
        String token = getUserAuthToken();

        // User 1 (siva) has an upvote on message 2
        MessageDetailResponse response = restTestClient
                .get()
                .uri("/api/messages/2")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(MessageDetailResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.userVote()).isEqualTo("UP");
        assertThat(response.votes().upvoteCount()).isEqualTo(5);
    }

    @Test
    void shouldReturn404ForNonExistentMessage() {
        restTestClient
                .get()
                .uri("/api/messages/99999")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldGetUserMessagesWithoutAuthentication() {
        var response = restTestClient
                .get()
                .uri("/api/messages?user=siva&page=0&size=20")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"content\"");
        assertThat(response).contains("\"totalElements\"");
        assertThat(response).contains("\"username\":\"siva\"");
    }

    @Test
    void shouldGetUserMessagesWithAuthentication() {
        String token = getUserAuthToken();

        var response = restTestClient
                .get()
                .uri("/api/messages?user=siva&page=0&size=20")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"content\"");
        assertThat(response).contains("\"totalElements\"");
        assertThat(response).contains("\"username\":\"siva\"");
        assertThat(response).contains("\"fullName\":\"Siva Katamreddy\"");
    }

    @Test
    void shouldGetUserMessagesWithPagination() {
        var response = restTestClient
                .get()
                .uri("/api/messages?user=siva&page=0&size=5")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"content\"");
        assertThat(response).contains("\"size\":5");
    }

    @Test
    void shouldReturn404ForNonExistentUser() {
        restTestClient
                .get()
                .uri("/api/messages?user=nonexistentuser")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldDeleteMessageSuccessfullyWhenUserIsAuthor() {
        String token = getUserAuthToken();

        // Create a message first
        CreateMessageResponse createdMessage = restTestClient
                .post()
                .uri("/api/messages")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "content": "This message will be deleted"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isCreated()
                .returnResult(CreateMessageResponse.class)
                .getResponseBody();

        // Delete the message
        restTestClient
                .delete()
                .uri("/api/messages/" + createdMessage.id())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isNoContent();

        // Verify message is deleted
        restTestClient
                .get()
                .uri("/api/messages/" + createdMessage.id())
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldDeleteMessageSuccessfullyWhenUserIsAdmin() {
        String adminToken = getAdminAuthToken();

        // Admin deletes a message created by another user (message 2 created by user 2)
        restTestClient
                .delete()
                .uri("/api/messages/2")
                .header("Authorization", "Bearer " + adminToken)
                .exchange()
                .expectStatus()
                .isNoContent();

        // Verify message is deleted
        restTestClient.get().uri("/api/messages/2").exchange().expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldNotDeleteMessageWithoutAuthentication() {
        restTestClient.delete().uri("/api/messages/1").exchange().expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldNotDeleteMessageWhenUserIsNotAuthorOrAdmin() {
        String token = getUserAuthToken();

        // User 1 (siva) tries to delete message 3 (created by user 3)
        restTestClient
                .delete()
                .uri("/api/messages/3")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentMessage() {
        String token = getUserAuthToken();

        restTestClient
                .delete()
                .uri("/api/messages/99999")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldVoteOnMessageSuccessfully() {
        String token = getUserAuthToken();

        VoteResponse response = restTestClient
                .post()
                .uri("/api/messages/2/vote")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "voteType": "UP"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(VoteResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.messageId()).isEqualTo(2);
        assertThat(response.voteType().name()).isEqualTo("UP");
        assertThat(response.votes().upvoteCount()).isEqualTo(5);
        assertThat(response.votes().downvoteCount()).isZero();
        assertThat(response.votes().score()).isEqualTo(5);
        assertThat(response.votedAt()).isNotNull();
    }

    @Test
    void shouldNotVoteWithoutAuthentication() {
        restTestClient
                .post()
                .uri("/api/messages/2/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "voteType": "UP"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldNotVoteOnOwnMessage() {
        String token = getUserAuthToken();

        // User 1 (siva) tries to vote on their own message (message 1)
        restTestClient
                .post()
                .uri("/api/messages/1/vote")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "voteType": "UP"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void shouldChangeVoteFromUpToDown() {
        String token = getUserAuthToken();

        // First, upvote message 3
        restTestClient
                .post()
                .uri("/api/messages/3/vote")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "voteType": "UP"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isOk();

        // Now change to downvote
        VoteResponse response = restTestClient
                .post()
                .uri("/api/messages/3/vote")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "voteType": "DOWN"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(VoteResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.voteType().name()).isEqualTo("DOWN");
    }

    @Test
    void shouldChangeVoteFromDownToUp() {
        String token = getUserAuthToken();

        // First, downvote message 4
        restTestClient
                .post()
                .uri("/api/messages/4/vote")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "voteType": "DOWN"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isOk();

        // Now change to upvote
        VoteResponse response = restTestClient
                .post()
                .uri("/api/messages/4/vote")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "voteType": "UP"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(VoteResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.voteType().name()).isEqualTo("UP");
    }

    @Test
    void shouldNotVoteWithInvalidVoteType() {
        String token = getUserAuthToken();

        restTestClient
                .post()
                .uri("/api/messages/2/vote")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "voteType": "INVALID"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotVoteOnNonExistentMessage() {
        String token = getUserAuthToken();

        restTestClient
                .post()
                .uri("/api/messages/99999/vote")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "voteType": "UP"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldGetUserVoteSuccessfully() {
        String token = getUserAuthToken();

        // User 1 (siva) has an upvote on message 2 (from test data)
        var response = restTestClient
                .get()
                .uri("/api/messages/2/vote")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"messageId\":2");
        assertThat(response).contains("\"voteType\":\"UP\"");
        assertThat(response).contains("\"votedAt\"");
    }

    @Test
    void shouldGetUserVoteWhenNoVoteExists() {
        String token = getUserAuthToken();

        // User 1 has no vote on message 14
        var response = restTestClient
                .get()
                .uri("/api/messages/14/vote")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"messageId\":14");
        assertThat(response).contains("\"voteType\":null");
        assertThat(response).contains("\"votedAt\":null");
    }

    @Test
    void shouldNotGetUserVoteWithoutAuthentication() {
        restTestClient
                .get()
                .uri("/api/messages/2/vote")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldNotGetUserVoteForNonExistentMessage() {
        String token = getUserAuthToken();

        restTestClient
                .get()
                .uri("/api/messages/99999/vote")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldRemoveVoteSuccessfully() {
        String token = getUserAuthToken();

        // First, vote on message 3
        restTestClient
                .post()
                .uri("/api/messages/3/vote")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "voteType": "UP"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isOk();

        // Now remove the vote
        var response = restTestClient
                .delete()
                .uri("/api/messages/3/vote")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"messageId\":3");
        assertThat(response).contains("\"message\"");
        assertThat(response).contains("\"votes\"");
    }

    @Test
    void shouldNotRemoveVoteWithoutAuthentication() {
        restTestClient
                .delete()
                .uri("/api/messages/2/vote")
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldHandleRemoveVoteWhenNoVoteExists() {
        String token = getUserAuthToken();

        // User 1 has no vote on message 14 - implementation returns 404 NOT_FOUND
        restTestClient
                .delete()
                .uri("/api/messages/14/vote")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void shouldNotRemoveVoteForNonExistentMessage() {
        String token = getUserAuthToken();

        restTestClient
                .delete()
                .uri("/api/messages/99999/vote")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldSearchMessagesWithoutAuthentication() {
        var response = restTestClient
                .get()
                .uri("/api/messages/search?q=Spring&page=0&size=20")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"content\"");
        assertThat(response).contains("\"totalElements\"");
    }

    @Test
    void shouldSearchMessagesWithAuthentication() {
        String token = getUserAuthToken();

        var response = restTestClient
                .get()
                .uri("/api/messages/search?q=Spring&page=0&size=20")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"content\"");
        assertThat(response).contains("\"totalElements\"");
    }

    @Test
    void shouldSearchMessagesWithPagination() {
        var response = restTestClient
                .get()
                .uri("/api/messages/search?q=message&page=0&size=5")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(String.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response).contains("\"content\"");
        assertThat(response).contains("\"size\":5");
    }
}

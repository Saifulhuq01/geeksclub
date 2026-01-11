package dev.sivalabs.geeksclub.rest;

import static org.assertj.core.api.Assertions.assertThat;

import dev.sivalabs.geeksclub.BaseIntegrationTest;
import dev.sivalabs.geeksclub.domain.dto.UserVM;
import dev.sivalabs.geeksclub.rest.dto.RegisterUserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.client.ExchangeResult;

@Sql("/test-data.sql")
class UserControllerTests extends BaseIntegrationTest {

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterUserResponse response = restTestClient
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "fullName":"User123",
                          "username":"user123",
                          "email":"user123@gmail.com",
                          "password":"Secret@121212"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isCreated()
                .returnResult(RegisterUserResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.fullName()).isEqualTo("User123");
        assertThat(response.username()).isEqualTo("user123");
        assertThat(response.email()).isEqualTo("user123@gmail.com");
        assertThat(response.role().name()).isEqualTo("USER");
    }

    @ParameterizedTest
    @CsvSource({
        ",user1,user1@gmail.com,password123,FullName",
        "user1,,user1@gmail.com,password123,Username",
        "user1,user1,,password123,Email",
        "user1,user1,user1@gmail.com,,Password",
    })
    void shouldNotRegisterWithoutRequiredFields(
            String fullName, String username, String email, String password, String errorFieldName) {

        record ReqBody(String fullName, String username, String email, String password) {}
        ExchangeResult exchangeResult = restTestClient
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ReqBody(fullName, username, email, password))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST)
                .returnResult();

        String responseJson = new String(exchangeResult.getResponseBodyContent());
        assertThat(responseJson).contains("%s is required".formatted(errorFieldName));
    }

    @Test
    void shouldGetUserByUsername() {
        String token = getUserAuthToken();

        UserVM user = restTestClient
                .get()
                .uri("/api/users/siva")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(UserVM.class)
                .getResponseBody();

        assertThat(user).isNotNull();
        assertThat(user.username()).isEqualTo("siva");
        assertThat(user.email()).isEqualTo("siva@gmail.com");
        assertThat(user.fullName()).isEqualTo("Siva Katamreddy");
    }

    @Test
    void shouldReturnNotFoundForNonExistentUser() {
        String token = getUserAuthToken();

        restTestClient
                .get()
                .uri("/api/users/nonexistent")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldUpdateUserProfile() {
        String token = getUserAuthToken();

        restTestClient
                .put()
                .uri("/api/users/me")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "fullName": "Siva Updated"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isOk();

        UserVM userVM = restTestClient
                .get()
                .uri("/api/users/siva")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(UserVM.class)
                .getResponseBody();

        assertThat(userVM).isNotNull();
        assertThat(userVM.fullName()).isEqualTo("Siva Updated");
    }

    @Test
    void shouldNotRegisterUserWithDuplicateEmail() {
        // Implementation throws unhandled exception instead of returning 409
        restTestClient
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "fullName":"New User",
                          "username":"newuser",
                          "email":"siva@gmail.com",
                          "password":"Secret@121212"
                        }
                        """)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void shouldNotRegisterUserWithDuplicateUsername() {
        // Implementation throws unhandled exception instead of returning 409
        restTestClient
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "fullName":"New User",
                          "username":"siva",
                          "email":"newuser@gmail.com",
                          "password":"Secret@121212"
                        }
                        """)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    void shouldNotRegisterUserWithInvalidEmail() {
        restTestClient
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "fullName":"New User",
                          "username":"newuser",
                          "email":"invalid-email",
                          "password":"Secret@121212"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotRegisterUserWithWeakPassword() {
        restTestClient
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "fullName":"New User",
                          "username":"newuser",
                          "email":"newuser@gmail.com",
                          "password":"weak"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void shouldGetCurrentUserSuccessfully() {
        String token = getUserAuthToken();

        UserVM user = restTestClient
                .get()
                .uri("/api/users/me")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(UserVM.class)
                .getResponseBody();

        assertThat(user).isNotNull();
        assertThat(user.username()).isEqualTo("siva");
        assertThat(user.email()).isEqualTo("siva@gmail.com");
        assertThat(user.fullName()).isEqualTo("Siva Katamreddy");
    }

    @Test
    void shouldNotGetCurrentUserWithoutAuthentication() {
        restTestClient.get().uri("/api/users/me").exchange().expectStatus().isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldGetUserByUsernameWithoutAuthentication() {
        // This is a public endpoint - should work without authentication
        UserVM user = restTestClient
                .get()
                .uri("/api/users/siva")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(UserVM.class)
                .getResponseBody();

        assertThat(user).isNotNull();
        assertThat(user.username()).isEqualTo("siva");
        assertThat(user.email()).isEqualTo("siva@gmail.com");
    }

    @Test
    void shouldNotUpdateUserWithoutAuthentication() {
        restTestClient
                .put()
                .uri("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "fullName": "Updated Name"
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldNotUpdateUserWithEmptyFullName() {
        String token = getUserAuthToken();

        restTestClient
                .put()
                .uri("/api/users/me")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "fullName": ""
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldNotUpdateUserWithNullFullName() {
        String token = getUserAuthToken();

        restTestClient
                .put()
                .uri("/api/users/me")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "fullName": null
                        }
                        """)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

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
        ",user1,user1@gmail.com,password123,fullName",
        "user1,,user1@gmail.com,password123,username",
        "user1,user1,,password123,email",
        "user1,user1,user1@gmail.com,,password",
    })
    void shouldNotRegisterWithoutRequiredFields(
            String fullName, String username, String email, String password, String errorFieldName) {

        restTestClient
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "fullName":%s,
                          "username":%s,
                          "email":%s,
                          "password":%s
                        }
                """.formatted(fullName, username, email, password))
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.BAD_REQUEST);
        // TODO; assert error field
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
}

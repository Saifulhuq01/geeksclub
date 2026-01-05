package dev.sivalabs.geeksclub.users.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;

import dev.sivalabs.geeksclub.BaseIntegrationTest;
import dev.sivalabs.geeksclub.users.domain.dto.UserVM;
import dev.sivalabs.geeksclub.users.rest.dto.RegisterUserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@Sql("/test-data.sql")
class UserControllerTests extends BaseIntegrationTest {

    @Test
    void shouldRegisterUserSuccessfully() {
        MvcTestResult testResult = mvc.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "fullName":"User123",
                          "username":"user123",
                          "email":"user123@gmail.com",
                          "password":"Secret@121212"
                        }
                        """)
                .exchange();

        assertThat(testResult)
                .hasStatus(CREATED)
                .bodyJson()
                .convertTo(RegisterUserResponse.class)
                .satisfies(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.fullName()).isEqualTo("User123");
                    assertThat(response.username()).isEqualTo("user123");
                    assertThat(response.email()).isEqualTo("user123@gmail.com");
                    assertThat(response.role().name()).isEqualTo("USER");
                });
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
        MvcTestResult testResult = mvc.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "fullName":%s,
                          "username":%s,
                          "email":%s,
                          "password":%s
                        }
                        """.formatted(fullName, username, email, password))
                .exchange();

        assertThat(testResult).hasStatus(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldGetUserByUsername() {
        String token = getUserAuthToken();

        MvcTestResult testResult = mvc.get()
                .uri("/api/users/siva")
                .header("Authorization", "Bearer " + token)
                .exchange();

        assertThat(testResult).hasStatusOk().bodyJson().convertTo(UserVM.class).satisfies(user -> {
            assertThat(user).isNotNull();
            assertThat(user.username()).isEqualTo("siva");
            assertThat(user.email()).isEqualTo("siva@gmail.com");
            assertThat(user.fullName()).isEqualTo("Siva Katamreddy");
        });
    }

    @Test
    void shouldReturnNotFoundForNonExistentUser() {
        String token = getUserAuthToken();

        MvcTestResult testResult = mvc.get()
                .uri("/api/users/nonexistent")
                .header("Authorization", "Bearer " + token)
                .exchange();

        assertThat(testResult).hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldUpdateUserProfile() {
        String token = getUserAuthToken();

        MvcTestResult testResult = mvc.put()
                .uri("/api/users/me")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "fullName": "Siva Updated"
                        }
                        """)
                .exchange();

        assertThat(testResult).hasStatusOk();

        MvcTestResult getResult = mvc.get()
                .uri("/api/users/siva")
                .header("Authorization", "Bearer " + token)
                .exchange();

        assertThat(getResult).hasStatusOk().bodyJson().convertTo(UserVM.class).satisfies(user -> {
            assertThat(user.fullName()).isEqualTo("Siva Updated");
        });
    }
}

package dev.sivalabs.geeksclub.users.rest;

import static org.assertj.core.api.Assertions.assertThat;

import dev.sivalabs.geeksclub.BaseIntegrationTest;
import dev.sivalabs.geeksclub.users.rest.dto.LoginResponse;
import dev.sivalabs.geeksclub.users.rest.dto.RefreshTokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

class AuthControllerTests extends BaseIntegrationTest {

    @Test
    @DisplayName("Given valid credentials, user should be able to login successfully")
    void shouldLoginSuccessfully() {
        MvcTestResult testResult = mvc.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"%s",
                            "password":"%s"
                        }
                        """.formatted(USER_EMAIL, USER_PASSWORD))
                .exchange();

        assertThat(testResult)
                .hasStatusOk()
                .bodyJson()
                .convertTo(LoginResponse.class)
                .satisfies(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.fullName()).isEqualTo("Siva Katamreddy");
                    assertThat(response.email()).isEqualTo("siva@gmail.com");
                    assertThat(response.accessToken()).isNotBlank();
                    assertThat(response.refreshToken()).isNotBlank();
                });
    }

    @Test
    void shouldRefreshTokenSuccessfully() {
        MvcTestResult loginResult = mvc.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"%s",
                            "password":"%s"
                        }
                        """.formatted(USER_EMAIL, USER_PASSWORD))
                .exchange();

        LoginResponse loginResponse = loginResult
                .assertThat()
                .bodyJson()
                .convertTo(LoginResponse.class)
                .actual();
        String refreshToken = loginResponse.refreshToken();

        MvcTestResult testResult = mvc.post()
                .uri("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "refreshToken": "%s"
                        }
                        """.formatted(refreshToken))
                .exchange();

        assertThat(testResult)
                .hasStatusOk()
                .bodyJson()
                .convertTo(RefreshTokenResponse.class)
                .satisfies(response -> {
                    assertThat(response.accessToken()).isNotBlank();
                    assertThat(response.refreshToken()).isNotBlank();
                });
    }
}

package dev.sivalabs.geeksclub.rest;

import static org.assertj.core.api.Assertions.assertThat;

import dev.sivalabs.geeksclub.BaseIntegrationTest;
import dev.sivalabs.geeksclub.rest.dto.LoginResponse;
import dev.sivalabs.geeksclub.rest.dto.RefreshTokenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

class AuthControllerTests extends BaseIntegrationTest {

    @Test
    void shouldLoginSuccessfullyGivenValidCredentials() {
        MvcTestResult result = mvc.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "email":"%s",
                            "password":"%s"
                        }
                        """.formatted(USER_EMAIL, USER_PASSWORD))
                .exchange();

        LoginResponse response = result.assertThat()
                .hasStatusOk()
                .bodyJson()
                .convertTo(LoginResponse.class)
                .actual();

        assertThat(response).isNotNull();
        assertThat(response.fullName()).isEqualTo("Siva Katamreddy");
        assertThat(response.email()).isEqualTo("siva@gmail.com");
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
    }

    @Test
    void shouldRefreshTokensSuccessfullyGivenValidRefreshToken() {
        // 1. Login to get a refresh token
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
                .hasStatusOk()
                .bodyJson()
                .convertTo(LoginResponse.class)
                .actual();

        String refreshToken = loginResponse.refreshToken();

        // 2. Use the refresh token to get new tokens
        MvcTestResult refreshResult = mvc.post()
                .uri("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "refreshToken": "%s"
                        }
                        """.formatted(refreshToken))
                .exchange();

        RefreshTokenResponse response = refreshResult
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .convertTo(RefreshTokenResponse.class)
                .actual();

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
    }
}

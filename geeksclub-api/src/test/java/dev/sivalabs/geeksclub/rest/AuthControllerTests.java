package dev.sivalabs.geeksclub.rest;

import static org.assertj.core.api.Assertions.assertThat;

import dev.sivalabs.geeksclub.BaseIntegrationTest;
import dev.sivalabs.geeksclub.rest.dto.LoginResponse;
import dev.sivalabs.geeksclub.rest.dto.RefreshTokenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class AuthControllerTests extends BaseIntegrationTest {

    @Test
    void shouldLoginSuccessfullyGivenValidCredentials() {
        LoginResponse response = restTestClient
                .post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                            "email":"%s",
                            "password":"%s"
                        }
                        """.formatted(USER_EMAIL, USER_PASSWORD))
                .exchange()
                .returnResult(LoginResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.fullName()).isEqualTo("Siva Katamreddy");
        assertThat(response.email()).isEqualTo("siva@gmail.com");
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
    }

    @Test
    void shouldRefreshTokensSuccessfullyGivenValidRefreshToken() {
        String token = getUserAuthToken();

        RefreshTokenResponse response = restTestClient
                .post()
                .uri("/api/auth/refresh")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .returnResult(RefreshTokenResponse.class)
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.refreshToken()).isNotBlank();
    }
}

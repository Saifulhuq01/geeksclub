package dev.sivalabs.geeksclub.users.rest;

import static org.assertj.core.api.Assertions.assertThat;

import dev.sivalabs.geeksclub.BaseIntegrationTest;
import dev.sivalabs.geeksclub.users.rest.dto.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

class LoginControllerTests extends BaseIntegrationTest {

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
}

package dev.sivalabs.geeksclub.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import dev.sivalabs.geeksclub.domain.dto.AuthToken;
import dev.sivalabs.geeksclub.domain.service.AuthService;
import dev.sivalabs.geeksclub.rest.dto.RefreshTokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthControllerUnitTest {

    @Mock
    private AuthService authService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService);
    }

    @Test
    void shouldRefreshTokenSuccessfully() {
        String refreshToken = "valid-refresh-token";
        String newAccessToken = "new-access-token";
        String newRefreshToken = "new-refresh-token";
        AuthToken authToken =
                new AuthToken(newAccessToken, java.time.Instant.now(), newRefreshToken, java.time.Instant.now());

        given(authService.refreshAccessToken(refreshToken)).willReturn(authToken);

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        var response = authController.refreshToken(request);

        assertThat(response.accessToken()).isEqualTo(newAccessToken);
        assertThat(response.refreshToken()).isEqualTo(newRefreshToken);
    }
}

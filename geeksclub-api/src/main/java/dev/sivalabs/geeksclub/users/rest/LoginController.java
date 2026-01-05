package dev.sivalabs.geeksclub.users.rest;

import dev.sivalabs.geeksclub.users.domain.AuthService;
import dev.sivalabs.geeksclub.users.domain.dto.LoginCmd;
import dev.sivalabs.geeksclub.users.rest.dto.LoginRequest;
import dev.sivalabs.geeksclub.users.rest.dto.LoginResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Auth API")
class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final AuthService authService;

    LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/api/auth/login")
    LoginResponse login(@RequestBody @Valid LoginRequest req) {
        log.info("Login request for email: {}", req.email());
        var request = new LoginCmd(req.email(), req.password());
        var authResponse = authService.authenticate(request);
        return new LoginResponse(
                authResponse.accessToken(),
                authResponse.accessTokenExpiresAt(),
                authResponse.refreshToken(),
                authResponse.refreshTokenExpiresAt(),
                authResponse.fullName(),
                authResponse.username(),
                authResponse.email(),
                authResponse.role());
    }
}

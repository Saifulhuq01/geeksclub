package dev.sivalabs.geeksclub.domain.service;

import dev.sivalabs.geeksclub.domain.dto.AuthToken;
import dev.sivalabs.geeksclub.domain.dto.LoginCmd;
import dev.sivalabs.geeksclub.domain.dto.LoginResult;
import dev.sivalabs.geeksclub.domain.dto.UserVM;
import dev.sivalabs.geeksclub.domain.repo.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {
    private final AuthenticationManager authManager;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    AuthService(
            AuthenticationManager authManager,
            TokenProvider tokenProvider,
            UserRepository userRepository,
            UserEntityMapper userEntityMapper) {
        this.authManager = authManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    public LoginResult authenticate(LoginCmd request) {
        var auth = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        authManager.authenticate(auth);

        var user = userRepository
                .findByEmailIgnoreCase(request.email())
                .map(userEntityMapper::toUserVM)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.email()));

        var authToken = tokenProvider.generate(user);
        return new LoginResult(
                authToken.accessToken(),
                authToken.accessTokenExpiresAt(),
                authToken.refreshToken(),
                authToken.refreshTokenExpiresAt(),
                user.fullName(),
                user.username(),
                user.email(),
                user.role().name());
    }

    public AuthToken generateToken(UserVM user) {
        return tokenProvider.generate(user);
    }

    public AuthToken refreshAccessToken(String refreshToken) {
        var jwt = tokenProvider.validateToken(refreshToken);
        String email = jwt.getSubject();
        var user = userRepository
                .findByEmailIgnoreCase(email)
                .map(userEntityMapper::toUserVM)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return tokenProvider.generate(user);
    }
}

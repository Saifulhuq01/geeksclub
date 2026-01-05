package dev.sivalabs.geeksclub.users.domain;

import dev.sivalabs.geeksclub.users.domain.dto.LoginCmd;
import dev.sivalabs.geeksclub.users.domain.dto.LoginResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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
}

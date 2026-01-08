package dev.sivalabs.geeksclub.domain.service;

import dev.sivalabs.geeksclub.domain.dto.SecurityUser;
import dev.sivalabs.geeksclub.domain.dto.UserVM;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
class SecurityUserDetailsService implements UserDetailsService {
    private final UserService userService;

    SecurityUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String userName) {
        return userService
                .findByEmail(userName)
                .map(this::toSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("Email " + userName + " not found"));
    }

    private SecurityUser toSecurityUser(UserVM user) {
        return new SecurityUser(
                user.id(), user.fullName(), user.username(), user.email(), user.password(), user.role());
    }
}

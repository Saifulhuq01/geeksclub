package dev.sivalabs.geeksclub.users;

import dev.sivalabs.geeksclub.users.domain.Role;
import dev.sivalabs.geeksclub.users.domain.UserService;
import dev.sivalabs.geeksclub.users.domain.dto.AuthenticatedUser;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserContextUtils {
    private final UserService userService;

    public UserContextUtils(UserService userService) {
        this.userService = userService;
    }

    public AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return userService
                    .findByEmail(email)
                    .map(userVM -> new AuthenticatedUser(
                            userVM.id(), userVM.email(), userVM.fullName(), userVM.username(), userVM.role()))
                    .orElse(null);
        }
        return null;
    }

    public AuthenticatedUser getCurrentUserOrThrow() {
        var user = getCurrentUser();
        if (user != null) {
            return user;
        }
        throw new AccessDeniedException("User not logged in");
    }

    public Long getCurrentUserId() {
        var user = getCurrentUser();
        return user != null ? user.id() : null;
    }

    public Long getCurrentUserIdOrThrow() {
        Long userId = getCurrentUserId();
        if (userId != null) {
            return userId;
        }
        throw new AccessDeniedException("User not logged in");
    }

    public boolean isCurrentUserAdmin() {
        var user = getCurrentUser();
        return user != null && user.role() == Role.ADMIN;
    }
}

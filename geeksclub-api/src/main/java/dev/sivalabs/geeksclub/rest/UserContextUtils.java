package dev.sivalabs.geeksclub.rest;

import dev.sivalabs.geeksclub.domain.dto.AuthenticatedUser;
import dev.sivalabs.geeksclub.domain.dto.Role;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class UserContextUtils {

    public AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof Jwt jwt) {
            Long userId = jwt.getClaim("user_id");
            String email = jwt.getSubject();
            String username = jwt.getClaim("username");
            String fullName = jwt.getClaim("full_name");
            String roleStr = jwt.getClaim("roles");
            Role role = Role.valueOf(roleStr);

            return new AuthenticatedUser(userId, email, fullName, username, role);
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

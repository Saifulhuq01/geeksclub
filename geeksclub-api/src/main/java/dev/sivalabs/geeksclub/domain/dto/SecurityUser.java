package dev.sivalabs.geeksclub.domain.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUser implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String fullName;
    private final String username;
    private final String email;
    private final String password;
    private final Role role;

    public SecurityUser(Long id, String fullName, String username, String email, String password, Role role) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public @NonNull String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}

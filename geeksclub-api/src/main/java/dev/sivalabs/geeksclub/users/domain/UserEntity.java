package dev.sivalabs.geeksclub.users.domain;

import dev.sivalabs.geeksclub.shared.entity.BaseEntity;
import dev.sivalabs.geeksclub.shared.utils.AssertUtil;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
class UserEntity extends BaseEntity {

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Version
    private int version;

    protected UserEntity() {}

    public UserEntity(Long id, String fullName, String username, String email, String password, Role role) {
        this.id = AssertUtil.requireNotNull(id, "User ID cannot be null");
        this.fullName = AssertUtil.requireNotNull(fullName, "Full name cannot be null");
        this.username = AssertUtil.requireNotNull(username, "Username cannot be null");
        this.email = AssertUtil.requireNotNull(email, "Email cannot be null");
        this.password = AssertUtil.requireNotNull(password, "Password cannot be null");
        this.role = AssertUtil.requireNotNull(role, "Role cannot be null");
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}

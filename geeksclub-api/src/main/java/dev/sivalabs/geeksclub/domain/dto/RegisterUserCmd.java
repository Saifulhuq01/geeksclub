package dev.sivalabs.geeksclub.domain.dto;

import dev.sivalabs.geeksclub.domain.utils.AssertUtil;

public record RegisterUserCmd(String fullName, String username, String email, String password, Role role) {
    public RegisterUserCmd {
        AssertUtil.requireNotBlank(fullName, "Full name cannot be blank");
        AssertUtil.requireNotBlank(username, "Username cannot be blank");
        AssertUtil.requireNotBlank(email, "Email cannot be blank");
        AssertUtil.requireNotBlank(password, "Password cannot be blank");
        AssertUtil.requireNotNull(role, "Role cannot be null");
    }
}

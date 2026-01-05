package dev.sivalabs.geeksclub.users.domain.dto;

import dev.sivalabs.geeksclub.shared.utils.AssertUtil;

public record LoginCmd(String email, String password) {
    public LoginCmd {
        AssertUtil.requireNotNull(email, "Email cannot be null");
        AssertUtil.requireNotNull(password, "Password cannot be null");
    }
}

package dev.sivalabs.geeksclub.domain.dto;

import dev.sivalabs.geeksclub.domain.utils.AssertUtil;

public record LoginCmd(String email, String password) {
    public LoginCmd {
        AssertUtil.requireNotNull(email, "Email cannot be null");
        AssertUtil.requireNotNull(password, "Password cannot be null");
    }
}

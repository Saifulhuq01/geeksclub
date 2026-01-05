package dev.sivalabs.geeksclub.users.domain.dto;

import dev.sivalabs.geeksclub.shared.utils.AssertUtil;

public record UpdateUserCmd(String fullName) {
    public UpdateUserCmd {
        AssertUtil.requireNotBlank(fullName, "Full name cannot be blank");
    }
}

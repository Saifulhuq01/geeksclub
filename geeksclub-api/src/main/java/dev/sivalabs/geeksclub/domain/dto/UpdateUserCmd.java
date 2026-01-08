package dev.sivalabs.geeksclub.domain.dto;

import dev.sivalabs.geeksclub.domain.utils.AssertUtil;

public record UpdateUserCmd(String fullName) {
    public UpdateUserCmd {
        AssertUtil.requireNotBlank(fullName, "Full name cannot be blank");
    }
}

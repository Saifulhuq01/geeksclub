package dev.sivalabs.geeksclub.domain.dto;

import java.util.List;

public record MostActiveUsersVM(List<ActiveUserVM> users, int limit) {}

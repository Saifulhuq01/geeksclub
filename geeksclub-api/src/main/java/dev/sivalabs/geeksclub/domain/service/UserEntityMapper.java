package dev.sivalabs.geeksclub.domain.service;

import dev.sivalabs.geeksclub.domain.dto.UserVM;
import dev.sivalabs.geeksclub.domain.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
class UserEntityMapper {

    public UserVM toUserVM(UserEntity userEntity) {
        return toUserVM(userEntity, false);
    }

    public UserVM toUserVMWithPassword(UserEntity userEntity) {
        return toUserVM(userEntity, true);
    }

    public UserVM toUserVM(UserEntity userEntity, boolean includePassword) {
        return new UserVM(
                userEntity.getId(),
                userEntity.getFullName(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                includePassword ? userEntity.getPassword() : null,
                userEntity.getRole(),
                userEntity.getCreatedAt());
    }

    public UserEntity toEntity(UserVM user) {
        return new UserEntity(user.id(), user.fullName(), user.username(), user.email(), user.password(), user.role());
    }
}

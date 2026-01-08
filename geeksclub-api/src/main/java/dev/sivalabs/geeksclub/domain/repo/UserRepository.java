package dev.sivalabs.geeksclub.domain.repo;

import dev.sivalabs.geeksclub.domain.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByUsernameIgnoreCase(String username);

    Optional<UserEntity> findByUsernameIgnoreCase(String username);
}

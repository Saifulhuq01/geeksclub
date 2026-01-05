package dev.sivalabs.geeksclub.users.domain;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    UserService(UserRepository userRepository, UserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    public Optional<UserVM> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).map(userEntityMapper::toUserVM);
    }
}

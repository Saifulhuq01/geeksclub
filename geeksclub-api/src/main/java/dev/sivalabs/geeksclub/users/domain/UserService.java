package dev.sivalabs.geeksclub.users.domain;

import dev.sivalabs.geeksclub.shared.utils.IdGenerator;
import dev.sivalabs.geeksclub.users.domain.dto.RegisterUserCmd;
import dev.sivalabs.geeksclub.users.domain.dto.UpdateUserCmd;
import dev.sivalabs.geeksclub.users.domain.dto.UserVM;
import dev.sivalabs.geeksclub.users.domain.exception.EmailExistsException;
import dev.sivalabs.geeksclub.users.domain.exception.UserNotFoundException;
import dev.sivalabs.geeksclub.users.domain.exception.UsernameExistsException;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;

    UserService(UserRepository userRepository, UserEntityMapper userEntityMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserVM> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).map(userEntityMapper::toUserVMWithPassword);
    }

    public UserVM getByUsername(String username) {
        return userRepository
                .findByUsernameIgnoreCase(username)
                .map(userEntityMapper::toUserVM)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));
    }

    @Transactional
    public void registerUser(RegisterUserCmd cmd) {
        if (userRepository.existsByEmailIgnoreCase(cmd.email())) {
            throw new EmailExistsException("User with email '" + cmd.email() + "' already exists");
        }
        if (userRepository.existsByUsernameIgnoreCase(cmd.username())) {
            throw new UsernameExistsException("User with username '" + cmd.username() + "' already exists");
        }
        String passwordHash = passwordEncoder.encode(cmd.password());
        var user = new UserEntity(
                IdGenerator.generateLong(), cmd.fullName(), cmd.username(), cmd.email(), passwordHash, cmd.role());
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long userId, UpdateUserCmd cmd) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.updateFullName(cmd.fullName());
        userRepository.save(user);
    }
}

package dev.sivalabs.geeksclub.users.rest;

import static org.springframework.http.HttpStatus.CREATED;

import dev.sivalabs.geeksclub.users.UserContextUtils;
import dev.sivalabs.geeksclub.users.domain.Role;
import dev.sivalabs.geeksclub.users.domain.UserService;
import dev.sivalabs.geeksclub.users.domain.dto.RegisterUserCmd;
import dev.sivalabs.geeksclub.users.domain.dto.UpdateUserCmd;
import dev.sivalabs.geeksclub.users.domain.dto.UserVM;
import dev.sivalabs.geeksclub.users.rest.dto.RegisterUserRequest;
import dev.sivalabs.geeksclub.users.rest.dto.RegisterUserResponse;
import dev.sivalabs.geeksclub.users.rest.dto.UpdateUserRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users API")
class UserController {
    private final UserService userService;
    private final UserContextUtils userContextUtils;

    UserController(UserService userService, UserContextUtils userContextUtils) {
        this.userService = userService;
        this.userContextUtils = userContextUtils;
    }

    @PostMapping("")
    ResponseEntity<RegisterUserResponse> createUser(@RequestBody @Valid RegisterUserRequest req) {
        var cmd = new RegisterUserCmd(req.fullName(), req.username(), req.email(), req.password(), Role.USER);
        userService.registerUser(cmd);
        var response = new RegisterUserResponse(req.fullName(), req.username(), req.email(), Role.USER);
        return ResponseEntity.status(CREATED.value()).body(response);
    }

    @GetMapping("/{username}")
    ResponseEntity<UserVM> findUser(@PathVariable String username) {
        var user = userService.getByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{username}")
    @SecurityRequirement(name = "Bearer")
    ResponseEntity<Void> updateUser(@PathVariable String username, @RequestBody @Valid UpdateUserRequest request) {
        var user = userService.getByUsername(username);
        var currentUser = userContextUtils.getCurrentUserOrThrow();

        if (!currentUser.id().equals(user.id())) {
            throw new AccessDeniedException("You can only update your own profile");
        }
        var cmd = new UpdateUserCmd(request.fullName());
        userService.updateUser(currentUser.id(), cmd);
        return ResponseEntity.ok().build();
    }
}

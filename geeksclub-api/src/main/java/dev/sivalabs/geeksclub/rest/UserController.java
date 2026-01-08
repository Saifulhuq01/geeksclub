package dev.sivalabs.geeksclub.rest;

import static org.springframework.http.HttpStatus.CREATED;

import dev.sivalabs.geeksclub.domain.dto.RegisterUserCmd;
import dev.sivalabs.geeksclub.domain.dto.Role;
import dev.sivalabs.geeksclub.domain.dto.UpdateUserCmd;
import dev.sivalabs.geeksclub.domain.dto.UserVM;
import dev.sivalabs.geeksclub.domain.service.UserService;
import dev.sivalabs.geeksclub.rest.dto.RegisterUserRequest;
import dev.sivalabs.geeksclub.rest.dto.RegisterUserResponse;
import dev.sivalabs.geeksclub.rest.dto.UpdateUserRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/me")
    ResponseEntity<UserVM> findCurrentUser() {
        var currentUser = userContextUtils.getCurrentUserOrThrow();
        var user = userService.getByUsername(currentUser.username());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{username}")
    ResponseEntity<UserVM> findUser(@PathVariable String username) {
        var user = userService.getByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    @SecurityRequirement(name = "Bearer")
    ResponseEntity<Void> updateUser(@RequestBody @Valid UpdateUserRequest request) {
        var currentUser = userContextUtils.getCurrentUserOrThrow();
        var cmd = new UpdateUserCmd(request.fullName());
        userService.updateUser(currentUser.id(), cmd);
        return ResponseEntity.ok().build();
    }
}

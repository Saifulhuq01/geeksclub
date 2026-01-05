package dev.sivalabs.geeksclub.users.domain.exception;

import dev.sivalabs.geeksclub.shared.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

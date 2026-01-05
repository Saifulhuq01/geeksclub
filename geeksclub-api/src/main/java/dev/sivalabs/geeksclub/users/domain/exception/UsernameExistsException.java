package dev.sivalabs.geeksclub.users.domain.exception;

import dev.sivalabs.geeksclub.shared.exception.ResourceExistsException;

public class UsernameExistsException extends ResourceExistsException {
    public UsernameExistsException(String message) {
        super(message);
    }
}

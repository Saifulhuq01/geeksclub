package dev.sivalabs.geeksclub.users.domain.exception;

import dev.sivalabs.geeksclub.shared.exception.ResourceExistsException;

public class EmailExistsException extends ResourceExistsException {
    public EmailExistsException(String message) {
        super(message);
    }
}

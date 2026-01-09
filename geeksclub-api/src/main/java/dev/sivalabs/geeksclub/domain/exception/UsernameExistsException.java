package dev.sivalabs.geeksclub.domain.exception;

public class UsernameExistsException extends ResourceExistsException {
    public UsernameExistsException(String message) {
        super(message);
    }
}

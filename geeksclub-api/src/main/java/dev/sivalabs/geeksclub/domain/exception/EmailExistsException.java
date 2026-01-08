package dev.sivalabs.geeksclub.domain.exception;

public class EmailExistsException extends ResourceExistsException {
    public EmailExistsException(String message) {
        super(message);
    }
}

package dev.sivalabs.geeksclub.domain.exception;

public class BadRequestException extends DomainException {
    public BadRequestException(String message) {
        super(message);
    }
}

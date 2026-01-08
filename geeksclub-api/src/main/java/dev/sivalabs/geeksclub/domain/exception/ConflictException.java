package dev.sivalabs.geeksclub.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends DomainException {
    public ConflictException(String message) {
        super(message);
    }
}

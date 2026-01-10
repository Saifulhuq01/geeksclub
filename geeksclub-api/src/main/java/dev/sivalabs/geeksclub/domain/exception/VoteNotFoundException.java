package dev.sivalabs.geeksclub.domain.exception;

public class VoteNotFoundException extends ResourceNotFoundException {
    public VoteNotFoundException(String message) {
        super(message);
    }
}

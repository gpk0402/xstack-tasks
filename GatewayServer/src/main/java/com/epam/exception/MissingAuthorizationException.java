package com.epam.exception;

public class MissingAuthorizationException extends RuntimeException {
    public MissingAuthorizationException(String message) {
        super(message);
    }
}

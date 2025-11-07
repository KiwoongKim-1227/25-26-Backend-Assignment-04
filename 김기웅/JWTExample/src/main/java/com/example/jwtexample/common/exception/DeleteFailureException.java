package com.example.jwtexample.common.exception;

public class DeleteFailureException extends RuntimeException {
    public DeleteFailureException(String message) {
        super(message);
    }
}

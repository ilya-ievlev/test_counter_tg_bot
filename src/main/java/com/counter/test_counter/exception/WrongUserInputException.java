package com.counter.test_counter.exception;

public class WrongUserInputException extends RuntimeException {
    public WrongUserInputException() {
    }

    public WrongUserInputException(String message) {
        super(message);
    }

    public WrongUserInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongUserInputException(Throwable cause) {
        super(cause);
    }
}

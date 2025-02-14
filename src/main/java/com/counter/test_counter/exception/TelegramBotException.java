package com.counter.test_counter.exception;

public class TelegramBotException extends RuntimeException {
    public TelegramBotException(String message) {
        super(message);
    }

    public TelegramBotException() {
    }

    public TelegramBotException(Throwable cause) {
        super(cause);
    }

    public TelegramBotException(String message, Throwable cause) {
        super(message, cause);
    }
}

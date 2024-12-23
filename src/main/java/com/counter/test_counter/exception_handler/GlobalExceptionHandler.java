package com.counter.test_counter.exception_handler;

import com.counter.test_counter.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final TelegramBot telegramBot;
    private static final long WORK_CHAT_ID = -4511047196l; // todo move this to database

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(IllegalArgumentException e) {
        telegramBot.sendMessage(WORK_CHAT_ID, e.getMessage());
    }
}

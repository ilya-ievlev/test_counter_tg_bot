package com.counter.test_counter.exception_handler;

import com.counter.test_counter.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final TelegramBot telegramBot;
    private static final long WORK_CHAT_ID = -4511047196l; // todo move this to database


    @AfterThrowing(pointcut = "execution(* com.counter.test_counter..*(..))", throwing = "ex")
    public void logException(Exception ex) {
        log.error("Exception occurred: " + ex.getMessage(), ex);
        telegramBot.sendMessage(WORK_CHAT_ID, "произошла ошибка, пните админа для решения, заранее спасибо");
//        telegramBot.sendMessage(ADMIN_CHAT_ID, ex.getMessage());
    } // todo create function to send message to admin
}


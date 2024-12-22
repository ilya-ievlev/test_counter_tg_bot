package com.counter.test_counter.service.handler.private_chat_handler;

import com.counter.test_counter.service.handler.Handler;
import com.counter.test_counter.telegram.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class PrivateCallbackQueryHandler implements Handler {

    @Override
    public void handle(Update update, TelegramBot bot) {

    }
    }

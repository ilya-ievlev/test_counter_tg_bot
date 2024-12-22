package com.counter.test_counter.service.handler;

import com.counter.test_counter.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler {
    void handle(Update update, TelegramBot bot);
}

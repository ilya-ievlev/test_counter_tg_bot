package com.counter.test_counter.initializer;

import com.counter.test_counter.processor.TelegramBotProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
//@Slf4j
public class BotInitializer {

    private final TelegramBotProcessor bot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi((DefaultBotSession.class));
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            // TODO: 05.11.2024 should I throw runtimeException here?
//            log.error("something wrong with telegram api"+e.getMessage(), e);
        }
    }
}

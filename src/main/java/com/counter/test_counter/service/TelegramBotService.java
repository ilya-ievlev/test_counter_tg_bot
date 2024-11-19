package com.counter.test_counter.service;

import com.counter.test_counter.processor.TelegramBotProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramBotService {
    private final TelegramBotProcessor telegramBotProcessor;
    private final TestService testService;

    public void processGroupMessage(Update update) {
        String messageText = update.getMessage().getText();
        if (messageText.equals("/menu")) {
            telegramBotProcessor.sendMessage(update.getMessage().getChatId(), "I can't do anything here. Please, proceed to private messages or google sheets to change some information");
        }
        if (update.getMessage().getText().contains("#test") || update.getMessage().hasDocument()) {
            Document document = update.getMessage().getDocument();
            if (document.getMimeType().equals("image/jpg")) {
                telegramBotProcessor.saveDocumentLocally(update.getMessage());
            }
        }

    }


    public void processPrivateMessage(Update update) {

    }


}

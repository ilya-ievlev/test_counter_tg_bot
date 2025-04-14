package com.counter.test_counter.dispatcher;

import com.counter.test_counter.service.handler.group_chat_handler.GroupCallbackQueryHandler;
import com.counter.test_counter.service.handler.group_chat_handler.GroupCommandHandler;
import com.counter.test_counter.service.handler.group_chat_handler.GroupMessageHandler;
import com.counter.test_counter.service.handler.private_chat_handler.PrivateCallbackQueryHandler;
import com.counter.test_counter.service.handler.private_chat_handler.PrivateCommandHandler;
import com.counter.test_counter.service.handler.private_chat_handler.PrivateMessageHandler;
import com.counter.test_counter.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramUpdateDispatcher {
    private static final long WORK_CHAT_ID = -4511047196l; // TODO: 13.11.2024 probably move this to env variables or other class, to be able to change it quicker and no duplicates
    private final GroupMessageHandler groupMessageHandler;
    private final PrivateMessageHandler privateMessageHandler;
    private final GroupCallbackQueryHandler groupCallbackQueryHandler;
    private final PrivateCallbackQueryHandler privateCallbackQueryHandler;
    private final GroupCommandHandler groupCommandHandler;
    private final PrivateCommandHandler privateCommandHandler;

    public void distribute(Update update, TelegramBot telegramBot) {
        if (update == null || telegramBot == null) {
            throw new IllegalArgumentException("Update and TelegramBot must not be null");
        }
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getMessage().getChatId() == WORK_CHAT_ID) {
                groupCallbackQueryHandler.handle(update, telegramBot);
            } else if (update.getCallbackQuery().getMessage().getChatId() > 0) {
                privateCallbackQueryHandler.handle(update, telegramBot);
            }
        }
        if (update.hasMessage() || update.getMessage().getChatId() == WORK_CHAT_ID) {
            if (update.getMessage().getText() != null && update.getMessage().getText().charAt(0) == '/') { // todo check if this thing can throw nullpointer here
                groupCommandHandler.handle(update, telegramBot);
            } else {
                groupMessageHandler.handle(update, telegramBot);
            }
        } else if (update.hasMessage() || update.getMessage().getChatId() > 0) {
            if (update.getMessage().getText() != null && update.getMessage().getText().charAt(0) == '/') { // todo check if this thing can throw nullpointer here
                privateCommandHandler.handle(update, telegramBot);
            } else {
                privateMessageHandler.handle(update, telegramBot);
            }
        } else {
            telegramBot.sendMessage(update.getMessage().getChatId(), "я не могу обработать сообщения такого типа, пожалуйста, обратитесь к администратору");
            log.info("unknown update type: {}", update);
        }
    }
}

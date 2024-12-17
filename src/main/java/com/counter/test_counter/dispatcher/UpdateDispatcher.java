package com.counter.test_counter.dispatcher;

import com.counter.test_counter.telegram.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateDispatcher {
    private static final long WORK_CHAT_ID = -4511047196l; // TODO: 13.11.2024 probably move this to env variables or other class, to be able to change it quicker and no duplicates


    public void distribute(Update update, TelegramBot telegramBot) {
        if (update == null || telegramBot == null) {
            throw new IllegalArgumentException("Update and TelegramBot must not be null");
        }
        if (update.hasMessage() || update.getMessage().getChatId() == WORK_CHAT_ID){

        }else if(update.hasMessage()||update.getMessage().getChatId()>0){

        } else{
            telegramBot.sendMessage(update.getMessage().getChatId(), "я не буду работать в этом чате, пожалуйста, обратитесь к администратору для изменения чата");
        }
    }
}

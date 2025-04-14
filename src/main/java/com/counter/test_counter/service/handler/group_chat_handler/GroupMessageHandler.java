package com.counter.test_counter.service.handler.group_chat_handler;

import com.counter.test_counter.external_client.OcrClient;
import com.counter.test_counter.model.TestResult;
import com.counter.test_counter.model.User;
import com.counter.test_counter.service.handler.Handler;
import com.counter.test_counter.telegram.TelegramBot;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Component
public class GroupMessageHandler implements Handler {

    private final OcrClient ocrClient;

    public GroupMessageHandler(OcrClient ocrClient) {
        this.ocrClient = ocrClient;
    }

    // todo add a button under sent message that clarifies what user wants to do and delete previous message if needed

    @Override
    public void handle(Update update, TelegramBot telegramBot) {
        if ((update.getMessage().hasDocument() && update.getMessage().getCaption().contains("#тест")) || // todo what if there is no caption
                (update.getMessage().hasPhoto() && update.getMessage().getCaption().contains("#тест"))) {
            telegramBot.sendMessage(update.getMessage().getChatId(), "#тест найден, обрабатываю");
//            if (update.getMessage().hasEntities() || update.getMessage().getEntities().contains("user")) {
//                telegramBot.sendMessage(update.getMessage().getChatId(), "отметка пользователя найдена ");
//            }

//            processImageWithHashtag(update.getMessage());
//
//            processTestHashtag(update.getMessage());
            Optional<File> downloadedFileOptional = telegramBot.downloadImage(update.getMessage());
            if (downloadedFileOptional.isEmpty()) {
                telegramBot.sendMessage(update.getMessage().getChatId(), "Не удалось загрузить файл из сообщения, обратитесь к администратору");
                return;
            }
            File downloadedFile = downloadedFileOptional.get();
            Optional<String> result = ocrClient.getResult(downloadedFile); // todo change this logic
            System.out.println(result.toString());
        }
    }

        private void processImageWithHashtag(Message message){
            TestResult testResult = new TestResult();
            testResult.setMessageId(message.getMessageId());
            testResult.setDateOfResult(new java.util.Date());
//            testResult.setExecutor

        }

        private Optional<User> getExecutorIfExists(Message message) {
            List<MessageEntity> entities;
            if (!message.getEntities().isEmpty()) {
                entities = message.getEntities();
            } else if (!message.getCaptionEntities().isEmpty()) {
                entities = message.getCaptionEntities();
            } else {
                return Optional.empty();
            }
            for (MessageEntity entity : entities) {
                if (entity.getType().equals("mention")) {
                    entity.getUser();
                }

            }

return Optional.empty();
        }}

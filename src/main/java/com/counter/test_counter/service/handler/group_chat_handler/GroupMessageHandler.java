package com.counter.test_counter.service.handler.group_chat_handler;

import com.counter.test_counter.external_client.OcrClient;
import com.counter.test_counter.model.TestResult;
import com.counter.test_counter.model.User;
import com.counter.test_counter.service.UserService;
import com.counter.test_counter.service.handler.Handler;
import com.counter.test_counter.telegram.TelegramBot;
import com.counter.test_counter.validator.TestMessageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GroupMessageHandler implements Handler {

    private final OcrClient ocrClient;
    private final UserService userService;
    private final TestMessageValidator testMessageValidator;


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
            File downloadedFile = telegramBot.downloadImage(update.getMessage());
            String result = ocrClient.getResult(downloadedFile); // todo change this logic
            System.out.println(result);
            processImageWithHashtag(update.getMessage(), downloadedFile, result, telegramBot); // todo change this
        }
    }

    private void processImageWithHashtag(Message message, File downloadedFile, String ocrResult, TelegramBot telegramBot) {

        List<String> errorList = testMessageValidator.validateNumberOfMentionsInMessage(message);
        if (!errorList.isEmpty()) {
            telegramBot.sendMessage(message.getChatId(), errorList.toString());  // todo change this to a more user friendly message
            return;
        }
        //============================
        TestResult testResult = new TestResult();
        testResult.setMessageId(message.getMessageId());
        testResult.setDateOfResult(new java.util.Date());
        //============================ // todo move this part away
        Optional<MessageEntity> executorMessageEntityOptional = message.getCaptionEntities().stream()
                .filter(entity -> entity.getType().equals("mention") || entity.getType().equals("text_mention"))
                .findFirst();
        if (executorMessageEntityOptional.isPresent()) {
            MessageEntity executorEntity = executorMessageEntityOptional.get();
            if (executorEntity.getText().charAt(0) == '@') {
                Optional<User> userOptional = userService.findByUsername(executorEntity.getText().substring(1));
                if (userOptional.isEmpty()) {
                    telegramBot.sendMessage(message.getChatId(), "Пользователь не найден, не является частью групы или не зарегестрирован в боте для подсчета тестов, обратитесь к администратору");
                    return;
                } else {
                    testResult.setExecutor(userOptional.get());
                }
            }
            if (executorEntity.getUser() != null) {
                Long userId = executorEntity.getUser().getId();
                Optional<User> userOptional = userService.findById(userId);
                if (userOptional.isEmpty()) {
                    telegramBot.sendMessage(message.getChatId(), "Пользователь не найден, не является частью групы или не зарегестрирован в боте для подсчета тестов, обратитесь к администратору");
                    return;
                } else {
                    testResult.setExecutor(userOptional.get());
                }
            }

        } else {
            Optional<User> userOptional = userService.findById(message.getFrom().getId());
            if (userOptional.isEmpty()) {
                telegramBot.sendMessage(message.getChatId(), "Пользователь не найден, не является частью групы или не зарегестрирован в боте для подсчета тестов, обратитесь к администратору");
                return;
            }
            testResult.setExecutor(userOptional.get());
        }
        Optional<User> uploadedByUser = userService.findById(message.getFrom().getId());
        if (uploadedByUser.isEmpty()) { // todo what can I do in this situation? exception would be great
            telegramBot.sendMessage(message.getChatId(), "Пользователь не найден, не является частью групы или не зарегестрирован в боте для подсчета тестов, обратитесь к администратору");
            return;
        }
        testResult.setUploadedBy(uploadedByUser.get());
        testResult.setMessageId(message.getMessageId());
        testResult.setMessageText(message.getCaption());
        testResult.setImageUrl(downloadedFile.getAbsolutePath());
        if (message.getDocument() != null) {
            testResult.setFileId(Long.valueOf(message.getDocument().getFileId())); // todo check if id is number, not include chars
        }
        if (message.getPhoto() != null) {
            testResult.setFileId(Long.valueOf(message.getPhoto().stream().findFirst().get().getFileId())); // todo check if this id is the largest number
        }
        testResult.setOcrResult(ocrResult);
        testResult.setChatId(String.valueOf(message.getChatId()));


    }
//
//    private Optional<User> getExecutorIfExists(Message message) {
//        List<MessageEntity> entities;
//        if (!message.getEntities().isEmpty()) {
//            entities = message.getEntities();
//        } else if (!message.getCaptionEntities().isEmpty()) {
//            entities = message.getCaptionEntities();
//        } else {
//            return Optional.empty();
//        }
//
//
//        for (MessageEntity entity : entities) {
//            if (entity.getType().equals("mention")) {
//                String userName = entity.getText();
//                Optional<User> userExecutorOptional = userService.findByUsername(userName);// todo use entityGraph here to avoid pulling hole user
//                if (userExecutorOptional.isEmpty()) {
//                    return Optional.empty();
//                }
//            }
//
//        }
//
//        return Optional.empty();
//    }
}

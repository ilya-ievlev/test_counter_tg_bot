package com.counter.test_counter.telegram;

import com.counter.test_counter.dispatcher.TelegramUpdateDispatcher;
import com.counter.test_counter.exception.TelegramBotException;
import com.counter.test_counter.exception.WrongUserInputException;
import com.counter.test_counter.model.TestResult;
import com.counter.test_counter.telegram.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot { // TODO: 14.11.2024 change name

    private static final long WORK_CHAT_ID = -4511047196l; // TODO: 13.11.2024 probably move this to env variables or other class, to be able to change it quicker
    private final BotConfig botConfig;
    private final TelegramUpdateDispatcher telegramUpdateDispatcher;

    @PostConstruct
    private void initCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/menu", "menu of the bot"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage(), e);
        }
    }


    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }


    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
//        throw new IllegalArgumentException("this is a test exception");
        telegramUpdateDispatcher.distribute(update, this);
    }

//    private void processGroupMessage(Update update) {
//
//        if (update.getMessage().hasText() && update.getMessage().getText() != null) {
//            String messageText = update.getMessage().getText();
//            if (messageText.equals("/menu")) { // todo messageText is null somehow, if we insert text with image. then this text will be called caption, not text
//                sendMessage(update.getMessage().getChatId(), "I can't do anything here. Please, proceed to private messages or google sheets to change some information");
//            }
//        }

    //
//        }
//
//    }
    private void processImageWithHashtag(Message message) {
        TestResult testResult = new TestResult();
        testResult.setMessageId(message.getMessageId());
        testResult.setMessageText(message.getCaption());
//        testResult.setUploadedBy(message.getFrom());
        testResult.setDateOfResult(new java.util.Date());

    }


    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("something wrong with telegram api " + e.getMessage(), e);
        }
    }

    public java.io.File downloadImage(Message message) {
        if (message == null) {
            log.error("message is null");
            throw new IllegalArgumentException("message must not be null");
        }
        if (message.getMediaGroupId() != null) {
            sendMessage(WORK_CHAT_ID, "я тебе несколько картинок пачкой обрабатывать не буду, давай по одной, плз");
            log.info("multiple images in one message, not supported yet");
        }

        if (message.hasDocument()) {
            Document document = message.getDocument();
            switch (document.getMimeType()) {
                case "image/jpeg":
                case "": // todo add more extensions
                    return saveFileLocally(message);
                default:
                    sendMessage(WORK_CHAT_ID, "ты мне скинул какую то дичь, я не могу работать с " + document.getMimeType());
            }
        } else if (message.hasPhoto()) {
            return saveFileLocally(message);
        } else {
            throw new TelegramBotException("no image found in message");
        }
        return null; // todo check for unreachable statement
    }


    public void forwardMessageFromWorkGroupToPrivateChat(Integer groupMessageIdToForward, Long privateChatId) { // TODO: 18.11.2024 what if this message was deleted or changed, Maybe better choice would be to duplicate all required data manually (forwarding is only to be able to understand context of the dialogue in group)
        ForwardMessage forwardMessage = new ForwardMessage();
        forwardMessage.setChatId(String.valueOf(privateChatId));
        forwardMessage.setFromChatId(String.valueOf(WORK_CHAT_ID));
        forwardMessage.setMessageId(groupMessageIdToForward);
        try {
            execute(forwardMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

//    private void saveFileLocally(Message message) {
//        String fileId = null;
//        if (message.hasDocument()) {
//            fileId = message.getDocument().getFileId();
//        }
//        if (message.hasPhoto()) {
//            List<PhotoSize> photoSizes = message.getPhoto();
//            fileId = photoSizes.get(photoSizes.size() - 1).getFileId();
//        }
//        if (fileId == null) {
//            throw new WrongUserInputException("no file found in message"); // todo check if this is correct to throw exception here or just log it
//        }
//        try {
//            GetFile getFile = new GetFile();
//            getFile.setFileId(fileId);
//            File file = execute(getFile);  // This is the method to get the file from Telegram servers
//            String destinationFolder = "downloads/";
//            try {
//                String fileUrl = "https://api.telegram.org/file/bot" + getBotToken() + "/" + file.getFilePath();
//                InputStream in = new URL(fileUrl).openStream();
//                java.io.File targetFile = new java.io.File(destinationFolder + file.getFilePath().substring(file.getFilePath().lastIndexOf("/") + 1));
//                java.io.File dir = new java.io.File(destinationFolder);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//
//                Files.copy(in, targetFile.toPath());
//                in.close();
//
//                log.info("File saved to: " + targetFile.getAbsolutePath() + ", file name: " + targetFile.getName());
//            } catch (IOException e) {
//                log.error(e.getMessage(), e);
//            }
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }

    private java.io.File saveFileLocally(Message message) {
        String fileId = null;
        if (message.hasDocument()) {
            fileId = message.getDocument().getFileId();
        }
        if (message.hasPhoto()) {
            List<PhotoSize> photoSizes = message.getPhoto();
            fileId = photoSizes.get(photoSizes.size() - 1).getFileId();
        }
        if (fileId == null) {
            throw new WrongUserInputException("no file found in message"); // todo check if this is correct to throw exception here or just log it
        }

        // todo temporary solution, need to refactor
        sendMessage(WORK_CHAT_ID, fileId);

        try {
            GetFile getFile = new GetFile();
            getFile.setFileId(fileId);
            File file = execute(getFile);  // This is the method to get the file from Telegram servers
            String destinationFolder = "downloads/";
            try {
                String fileUrl = "https://api.telegram.org/file/bot" + getBotToken() + "/" + file.getFilePath();
                InputStream in = new URL(fileUrl).openStream();
                java.io.File targetFile = new java.io.File(destinationFolder + file.getFilePath().substring(file.getFilePath().lastIndexOf("/") + 1));
                java.io.File dir = new java.io.File(destinationFolder);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                if (targetFile.exists()) {
                    in.close();
                    // todo remove, this is temporary solution
                    sendMessage(WORK_CHAT_ID, "файл уже существует, не буду его перезаписывать");
                    return targetFile;
                }
                Files.copy(in, targetFile.toPath()); // todo make sure that this thing throws exception if file already exists
                in.close();

                log.info("File saved to: " + targetFile.getAbsolutePath() + ", file name: " + targetFile.getName());
                return targetFile;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}


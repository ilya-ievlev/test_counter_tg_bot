package com.counter.test_counter.processor;

import com.counter.test_counter.config.BotConfig;
import com.counter.test_counter.service.TelegramBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
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

@Component
@Slf4j
@RequiredArgsConstructor
public class TelegramBotProcessor extends TelegramLongPollingBot { // TODO: 14.11.2024 change name

    private static final long WORK_CHAT_ID = -4511047196l; // TODO: 13.11.2024 probably move this to env variables or other class, to be able to change it quicker
    private final BotConfig botConfig;
    private final TelegramBotService telegramBotService;

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
        if (update.getMessage().getChat().getId() == WORK_CHAT_ID) { // TODO: 18.11.2024 what if there is no message in update?
            telegramBotService.processGroupMessage(update);
        } else if (update.getMessage().getChat().getId() > 0) {
            telegramBotService.processPrivateMessage(update);
        } else {
            sendMessage(update.getMessage().getChatId(), "can't process this chat");
        }
    }

//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage() && update.hasMessage()) {
//            String messageText = update.getMessage().getText();
//            long chatId = update.getMessage().getChatId();
//            System.out.println(update);
//            switch (messageText) {
//                case "/start":
//                    startCommandReceived(chatId, update.getMessage().getFrom().getUserName());
//                    break;
//
//                default:
//                    sendMessage(chatId, "sorry, command was not recognized: " + update.getMessage().getText());
//            }
//        }
//    }

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

    public void saveDocumentLocally(Message message) {
        Document document = message.getDocument();
        if (document != null && document.getFileName().endsWith(".jpg")) { // TODO: 18.11.2024 add another extensions
            try {
                String fileId = document.getFileId();
                GetFile getFile = new GetFile();
                getFile.setFileId(fileId);
                File file = execute(getFile);  // This is the method to get the file from Telegram servers
                downloadFile(file, "downloads/");
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


//    public void forwardMessageToPrivateChat(Message groupMessage, Long privateChatId) {
//        ForwardMessage forwardMessage = new ForwardMessage();
//        forwardMessage.setChatId(String.valueOf(privateChatId));
//        forwardMessage.setFromChatId(String.valueOf(groupMessage.getChatId()));
//        forwardMessage.setMessageId(groupMessage.getMessageId());
//        try {
//            execute(forwardMessage);
//        } catch (TelegramApiException e) {
//            log.error(e.getMessage(), e);
//        }
//    }

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

    private void downloadFile(File file, String destinationFolder) {
        try {
            String fileUrl = "https://api.telegram.org/file/bot" + getBotToken() + "/" + file.getFilePath();
            InputStream in = new URL(fileUrl).openStream();
            java.io.File targetFile = new java.io.File(destinationFolder + file.getFilePath().substring(file.getFilePath().lastIndexOf("/") + 1));
            java.io.File dir = new java.io.File(destinationFolder);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Files.copy(in, targetFile.toPath());
            in.close();

            log.info("File saved to: " + targetFile.getAbsolutePath() + ", file name: " + targetFile.getName());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}

package com.counter.test_counter.util;

import com.counter.test_counter.model.User;

public class TelegramUserToUserConverter {

    public static User convert(org.telegram.telegrambots.meta.api.objects.User telegramUser) {
        User user = new User();

        user.setFirstName(telegramUser.getFirstName());
        user.setLastName(telegramUser.getLastName());
        user.setUsername(telegramUser.getUserName());
        return user;
    }
}

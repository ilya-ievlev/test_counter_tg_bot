package com.counter.test_counter.validator;

import com.counter.test_counter.model.User;
import com.counter.test_counter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TestMessageValidator {


    public List<String> validateNumberOfMentionsInMessage(Message message) { // if list is empty, then message is valid
        List<String> errors = new ArrayList<>();
        if (message == null) {
            log.error("message is null"); // todo what can i do here, there will be a crush if not handled properly
            errors.add("message is null");
            return errors;
        }
        validateCaptionMentions(message.getCaptionEntities(), errors);
        return errors;
    }

    private void validateCaptionMentions(List<MessageEntity> captions, List<String> errors) {
        if (captions != null) {
            int numberOMentions = (int) captions.stream()
                    .filter(entity -> entity.getType().equals("mention") || entity.getType().equals("text_mention"))
                    .count();
            if (numberOMentions > 1) {
                errors.add("more than 1 user mentioned in message, can't process this correctly");
            }
        }
    }

//    private MessageEntity getMentionEntityFromEntities(List<MessageEntity> initialList) {
//        return initialList.stream()
//                .filter(entity -> entity.getType().equals("mention") || entity.getType().equals("text_mention"))
//                .findFirst().orElse(null); // null is unreachable because we already checked if there is at least one mention
//    }
}

package com.serezka.telegram.session;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Deque;
import java.util.LinkedList;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class SessionConfiguration {
    boolean saveUsersMessages = true, saveBotsMessages = true;
    boolean canEditMessages = true;
    final Deque<Step> steps = new LinkedList<>();

    public SessionConfiguration execute(Step step) {
        steps.add(step);
        return this;
    }

    public SessionConfiguration saveUsersMessages(boolean val) {
        this.saveUsersMessages = val;
        return this;
    }

    public SessionConfiguration saveBotsMessages(boolean val) {
        this.saveBotsMessages = val;
        return this;
    }

    public SessionConfiguration canEditMessages(boolean val) {
        this.canEditMessages = val;
        return this;
    }

    public SessionConfiguration get(String text, ReplyKeyboard replyKeyboard) {
        steps.add((session, update) -> session.send(SendMessage.builder()
                .chatId(update)
                .text(text).replyMarkup(replyKeyboard)
                .build())
        );
        return this;
    }

    public static SessionConfiguration create() {
        return new SessionConfiguration();
    }
}

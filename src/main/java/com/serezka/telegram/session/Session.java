package com.serezka.telegram.session;

import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.util.Keyboard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.function.TriConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Session class
 * @version 1.0
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@Log4j2
public class Session {
    static int idCounter = 0;

    Deque<TriConsumer<Bot, Update, Session>> input = new ArrayDeque<>();
    Deque<TriConsumer<Bot, Update, Session>> trash = new ArrayDeque<>();

    Bot bot;
    long chatId;

    // session variables
    Deque<Integer> botsMessagesIds = new ArrayDeque<>();
    @NonFinal @Setter boolean saveBotsMessages = true;

    Deque<Integer> usersMessagesIds = new ArrayDeque<>();
    @NonFinal @Setter boolean saveUsersMessages = true;

    long id = idCounter++;

    List<String> history = new LinkedList<>();

    public Session(Bot bot, long chatId) {
        this.bot = bot;
        this.chatId = chatId;
    }

    /**
     * get next step
     * @param bot    - bot
     * @param update - update
     */
    public void next(Bot bot, Update update) {
        if (!saveBotsMessages) bot.executeAsync(DeleteMessage.builder()
                .chatId(chatId).messageId(Objects.requireNonNull(botsMessagesIds.pollLast()))
                .build());

        if (!saveUsersMessages) bot.executeAsync(DeleteMessage.builder()
                .chatId(chatId).messageId(Objects.requireNonNull(usersMessagesIds.pollLast()))
                .build());

        usersMessagesIds.add(update.getMessageId());

        getNext(bot, update);
    }

    /**
     * get next step
     * @param bot - bot
     * @param update - update
     */
    protected void getNext(Bot bot, Update update) {
        if (input.isEmpty()) {
            log.warn("Session {} has no input", id);
            destroy(bot, update);
            return;
        }

        trash.add(input.pop());
        Objects.requireNonNull(trash.peek()).accept(bot, update, this);
    }

    /**
     * execute session
     * @param bot - bot
     * @param update - update
     */
    protected void destroy(Bot bot, Update update) {
        // delete users messages
        if (!saveUsersMessages) usersMessagesIds.forEach(
                messageId -> bot.executeAsync(DeleteMessage.builder()
                        .chatId(update.getChatId()).messageId(messageId)
                        .build()));

        if (!saveBotsMessages) botsMessagesIds.forEach(
                messageId -> bot.executeAsync(DeleteMessage.builder()
                        .chatId(update.getChatId()).messageId(messageId)
                        .build()));

        // remove session from session manager
        SessionManager.removeSession(chatId);

    }

    // session configuration

    // todo make appends functions

    public Session saveBotsMessages(boolean val) {
        this.saveBotsMessages = val;
        return this;
    }

    public Session saveUsersMessages(boolean val) {
        this.saveUsersMessages = val;
        return this;
    }

    public Session send(TriConsumer<Bot, Update, Session> function) {
        input.add(function);
        return this;
    }

    public Session send(String text, ReplyKeyboard replyKeyboard) {
        input.add((bot, update, session) -> bot.executeAsync(SendMessage.builder()
                .text(text).chatId(chatId)
                .replyMarkup(replyKeyboard)
                .build(), session));
        return this;
    }

    public Session send(String text) {
        send(text, Keyboard.Reply.DEFAULT);
        return this;
    }

    public Session get(String text, ReplyKeyboard replyKeyboard) {
        bot.executeAsync(SendMessage.builder()
                .text(text).chatId(chatId)
                .replyMarkup(replyKeyboard)
                .build());
        return this;
    }

    public Session get(String text) {
        get(text, Keyboard.Reply.DEFAULT);
        return this;
    }

    public Session tryGet(String text, ReplyKeyboard replyKeyboard, Function<Update, Boolean> function) {

        return this;
    }

    public Session tryGet(String text, Function<Update, Boolean> function) {
        tryGet(text, Keyboard.Reply.DEFAULT, function);
        return this;
    }

    public Session rollbackIf(BiFunction<Bot, Update, Boolean> function) {
        // todo
        return this;
    }

    public void execute(TriConsumer<Bot, Update, Session> function) {
        input.add(function);
    }

    public Session menu() {
        // todo
        return this;
    }
}

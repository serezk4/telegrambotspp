package com.serezka.telegram.session;

import com.serezka.telegram.bot.Bot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 * Session class
 *
 * @version 1.0
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class Session {
    static int idCounter = 0;

    SessionConfiguration configuration;

    Deque<Step> input;
    Deque<Step> trash;

    Bot bot;
    long chatId;

    // session variables
    Deque<Integer> botsMessagesIds = new ArrayDeque<>();
    Deque<Integer> usersMessagesIds = new ArrayDeque<>();

    long id = idCounter++;

    List<String> history = new LinkedList<>();

    public Session(SessionConfiguration configuration, Bot bot, long chatId) {
        this.configuration = configuration;
        this.bot = bot;
        this.chatId = chatId;

        this.input = configuration.getSteps();
        this.trash = new LinkedList<>();
    }

    /**
     * get next step
     *
     * @param bot    - bot
     * @param update - update
     */
    public void next(Bot bot, Update update) {
        usersMessagesIds.add(update.getMessageId());

        // get next step
        getNext(bot, update);

        // remove last message from bot
        if (!configuration.isSaveBotsMessages() && !botsMessagesIds.isEmpty() && !configuration.isCanEditMessages())
            Optional.ofNullable(botsMessagesIds.pop()).ifPresent(messageId ->
                    bot.executeAsync(DeleteMessage.builder().chatId(chatId).messageId(messageId).build()));

        if (configuration.isCanEditMessages() && botsMessagesIds.size() > 1)
            IntStream.range(0, botsMessagesIds.size() - 2).forEach(i ->
                    bot.executeAsync(DeleteMessage.builder()
                            .chatId(chatId).messageId(Objects.requireNonNull(botsMessagesIds.pollFirst()))
                            .build()));

        // remove last message from user
        if (!configuration.isSaveUsersMessages() && !usersMessagesIds.isEmpty())
            Optional.ofNullable(usersMessagesIds.pop()).ifPresent(messageId ->
                    bot.executeAsync(DeleteMessage.builder().chatId(chatId).messageId(messageId).build()));
    }

    // execute
    public void send(String text) {
        send(text, null);
    }

    public void send(String text, ReplyKeyboard replyKeyboard) {
        if (configuration.isCanEditMessages()) {
            send(EditMessageText.builder()
                    .chatId(chatId).messageId(Objects.requireNonNull(botsMessagesIds.peek()))
                    .text(text)
                    .build());

            if (replyKeyboard instanceof InlineKeyboardMarkup inlineKeyboard) {
                send(EditMessageReplyMarkup.builder()
                        .chatId(chatId).messageId(Objects.requireNonNull(botsMessagesIds.peek()))
                        .replyMarkup(inlineKeyboard)
                        .build());
            }

            return;
        }

        send(SendMessage.builder()
                .chatId(chatId)
                .text(text).replyMarkup(replyKeyboard)
                .build());

    }

    public void send(BotApiMethod<?> method) {
        bot.send(method).whenComplete((response, throwable) -> {
            if (throwable != null) {
                log.warn(throwable);
            }

            if (response instanceof Message message)
                botsMessagesIds.add(message.getMessageId());
        });
    }

    /**
     * get next step
     *
     * @param bot    - bot
     * @param update - update
     */
    protected void getNext(Bot bot, Update update) {
        log.info("trying to get next step for session {}", id);

        destroyIfEmpty(bot, update);

        log.info("Session {} has steps, remain {} | next: {}", id, input.size(), input.peek());

        trash.add(input.pop());
        Objects.requireNonNull(trash.peekLast()).accept(bot, update, this);

        destroyIfEmpty(bot, update);
    }

    protected void destroyIfEmpty(Bot bot, Update update) {
        if (!input.isEmpty()) return;

        log.info("Session {} has no input, it will be destroyed", id);
        destroy(bot, update);
    }

    /**
     * execute session
     *
     * @param bot    - bot
     * @param update - update
     */
    protected void destroy(Bot bot, Update update) {
        // delete users messages
        if (!configuration.isSaveUsersMessages()) usersMessagesIds.forEach(
                userMessageId -> bot.executeAsync(DeleteMessage.builder()
                        .chatId(update.getChatId()).messageId(userMessageId)
                        .build()));

        if (!configuration.isSaveBotsMessages()) botsMessagesIds.forEach(
                botMessageId -> bot.executeAsync(DeleteMessage.builder()
                        .chatId(update.getChatId()).messageId(botMessageId)
                        .build()));

        // remove session from session manager
        SessionManager.removeSession(chatId, this);
    }
}

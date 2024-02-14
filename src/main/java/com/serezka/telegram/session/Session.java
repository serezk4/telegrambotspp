package com.serezka.telegram.session;

import com.serezka.telegram.bot.Bot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
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

import java.util.*;

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
    Deque<Message> botMessages = new ArrayDeque<>();
    Deque<Message> userMessages = new ArrayDeque<>();

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
        userMessages.add(update.getMessage());

        // get next step
        getNext(bot, update);

        // remove last message from bot
        deleteMessages();
    }

    // execute methods
    public void append(String text, ReplyKeyboard replyKeyboard) {
        if (botMessages.isEmpty() || botMessages.peekLast() == null) send(text, replyKeyboard);

        botMessages.getLast().setText(botMessages.getLast().getText().replaceAll("\\.\\.\\.", "") + text);

        send(botMessages.getLast().getText(), replyKeyboard);
    }

    public void append(String text) {
        append(text, null);
    }

    public void send(String text) {
        send(text, null);
    }

    public void send(String text, ReplyKeyboard replyKeyboard) {
        // todo make check reply keyboard
        if (configuration.isCanEditMessages() && botMessages.peekLast() != null && botMessages.peekLast().getMessageId() != null) {
            send(EditMessageText.builder()
                    .chatId(botMessages.peekLast().getChatId()).messageId(botMessages.peekLast().getMessageId())
                    .text(text)
                    .build());

            if (replyKeyboard instanceof InlineKeyboardMarkup inlineKeyboard) {
                send(EditMessageReplyMarkup.builder()
                        .chatId(chatId).messageId(botMessages.peekLast().getMessageId())
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
                return;
            }

            if (response instanceof Message message) {
                if (method instanceof SendMessage sendMessage) {
                    message.setText(sendMessage.getText());
                    botMessages.add(message);
                    return;
                }
            }
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
        Objects.requireNonNull(trash.peekLast()).accept(this, update);

        destroyIfEmpty(bot, update);
    }

    protected void deleteMessages() {
        deleteUserMessages();
        deleteBotMessages();
    }

    protected void deleteUserMessages() {
        if (configuration.isSaveUsersMessages()) return;
        if (userMessages.isEmpty()) return;

        userMessages.stream().filter(userMessage -> !userMessage.isDeleted()).forEach(userMessage -> {
            userMessage.setDeleted(true);
            bot.executeAsync(DeleteMessage.builder()
                    .chatId(userMessage.getChatId()).messageId(userMessage.getMessageId())
                    .build());
        });
    }

    protected void deleteBotMessages() {
        if (configuration.isSaveBotsMessages()) return;
        if (botMessages.isEmpty()) return;

        while (botMessages.size() > 1) {
            Message temp = botMessages.pollFirst();

            bot.executeAsync(DeleteMessage.builder()
                    .chatId(temp.getChatId()).messageId(temp.getMessageId())
                    .build());
        }

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
        deleteMessages();

        // remove session from session manager
        SessionManager.removeSession(chatId, this);
    }
}

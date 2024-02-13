package com.serezka.telegram.session;

import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.util.Keyboard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter @Log4j2
public class Session {
    static int idCounter = 0;

    Deque<BiConsumer<Bot, Update>> input = new ArrayDeque<>();
    Deque<BiConsumer<Bot, Update>> trash = new ArrayDeque<>();

    Bot bot;
    long chatId;

    // session variables
    Queue<Integer> botsMessagesIds = new PriorityQueue<>();
    List<Integer> usersMessagesIds = new ArrayList<>();
    long id = idCounter++;
    @NonFinal @Setter boolean saveUsersMessages = true;

    List<String> history = new LinkedList<>();

    public Session(Bot bot, long chatId) {
        this.bot = bot;
        this.chatId = chatId;
    }

    /**
     * get next step
     * @param bot - bot
     * @param update - update
     */
    public void next(Bot bot, Update update) {
        usersMessagesIds.add(update.getMessageId());
        getNext(bot, update);
    }

    protected void getNext(Bot bot, Update update) {
        if (input.isEmpty()) {
            log.warn("Session {} has no input", id);
            destroy(bot, update);
            return;
        }

        trash.add(input.pop());
        Objects.requireNonNull(trash.peek()).accept(bot, update);
    }

    protected void destroy(Bot bot, Update update) {
        // delete users messages
        if (!saveUsersMessages) usersMessagesIds.forEach(
                messageId -> bot.executeAsync(DeleteMessage.builder()
                        .chatId(update.getChatId()).messageId(messageId)
                        .build()));

        // remove session from session manager
        // todo add menu manager instance here

    };

    // in-line creation
    public Session send(BiConsumer<Bot, Update> function) {
        input.add(function);
        return this;
    }

    public Session send(String text, ReplyKeyboard replyKeyboard) {
        input.add((bot, update) -> bot.executeAsync(SendMessage.builder()
                .text(text).chatId(chatId)
                .replyMarkup(replyKeyboard)
                .build()));
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
        // todo
        return this;
    }

    public Session tryGet(String text, Function<Update, Boolean> function) {
        tryGet(text, Keyboard.Reply.DEFAULT, function);
        return this;
    }

    public Session rollbackIf(Function<Update, Boolean> function) {
        // todo
        return this;
    }
}

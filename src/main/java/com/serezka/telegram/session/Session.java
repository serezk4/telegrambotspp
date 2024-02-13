package com.serezka.telegram.session;

import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.session.step.Step;
import com.serezka.telegram.util.Keyboard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter @Log4j2
public abstract class Session {
    static int idCounter = 0;

    // TODO fix this fucking shitcode

    final Deque<Function<Update, Pair<String, ReplyKeyboard>>> stepDeque = new ArrayDeque<>();

    Bot bot;
    long chatId;

    public Session(Bot bot, long chatId) {
        this.bot = bot;
        this.chatId = chatId;
    }

    // in-line creation
    public Session send(Function<Update, Pair<String, ReplyKeyboard>> function) {
        stepDeque.add(function);
        return this;
    }

    public Session send(String text, ReplyKeyboard replyKeyboard) {

        return this;
    }

    public Session send(String text) {
        send(text, Keyboard.Reply.DEFAULT);
        return this;
    }

    public Session get(String text, ReplyKeyboard replyKeyboard) {
        bot.executeAsync(SendMessage.builder().text(text).chatId(chatId).replyMarkup(replyKeyboard).build());
        return this;
    }

    public Session get(String text) {
        get(text, Keyboard.Reply.DEFAULT);
        return this;
    }

    public Session getWhileMatches(String text, ReplyKeyboard replyKeyboard, Function<Update, Boolean> function) {
        // todo
        return this;
    }

    public Session getWhileMatches(String text, Function<Update, Boolean> function) {
        getWhileMatches(text, Keyboard.Reply.DEFAULT, function);
        return this;
    }

    public Session rollbackIf(Function<Update, Boolean> function) {
        // todo
        return this;
    }

    // session variables
    private final Queue<Integer> botsMessagesIds = new PriorityQueue<>();
    private final List<Integer> usersMessagesIds = new ArrayList<>();
    final long id = idCounter++;
    @Setter boolean saveUsersMessages = true;

    private final List<String> history = new LinkedList<>();
    private boolean created;

    public void next(Bot bot, Update update) {
        usersMessagesIds.add(update.getMessageId());

        if (!created) {
            init(bot, update);
            created = true;
        } else getNext(bot, update);
    }

    protected abstract void init(Bot bot, Update update);
    protected abstract void getNext(Bot bot, Update update);
    protected void destroy(Bot bot, Update update) {
        // delete users messages
        if (!saveUsersMessages) usersMessagesIds.forEach(
                messageId -> bot.executeAsync(DeleteMessage.builder()
                        .chatId(update.getChatId()).messageId(messageId)
                        .build()));

        // remove session from session manager
        // todo add menu manager instance here

    };
}

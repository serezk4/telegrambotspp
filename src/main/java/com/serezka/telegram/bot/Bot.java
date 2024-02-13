package com.serezka.telegram.bot;

import com.serezka.database.model.History;
import com.serezka.database.service.MessageService;
import com.serezka.localization.Localization;
import com.serezka.telegram.session.Session;
import com.serezka.telegram.util.Keyboard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Main class for bot
 * Handles updates and transfer to handler
 * @see Handler
 * @version 1.0
 */
@Log4j2
@Getter @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Bot extends TelegramLongPollingBot {
    /* bot data */ String botUsername, botToken;
    /* handler */ Handler handler;
    /* executor */ ExecutorServiceRouter executor;
    /* localization */ Localization localization = Localization.getInstance();

    // entities
    MessageService messageService;

    public Bot(String botUsername, String botToken, int threadCount,
               MessageService messageService,
               Handler handler) {
        super(botToken);

        this.botUsername = botUsername;
        this.botToken = botToken;
        this.handler = handler;

        this.executor = new ExecutorServiceRouter(threadCount);

        this.messageService = messageService;
    }


    /**
     * Method for handling updates
     * @see Update
     * @param update Update received
     */
    @Override
    public void onUpdateReceived(Update update) {
        log.info("new update received");

        if (executor.isShutdown()) {
            log.info("user {} {} trying to make query", update.getUsername(), update.getChatId());
            send(SendMessage.builder()
                    .chatId(update).text(localization.get("bot.shutdown"))
                    .build());
            return;
        }

        messageService.save(new History(update.getChatId(), update.getChatId(), update.getQueryType(), update.getText()));
        executor.route(update.getChatId(), () -> handler.process(this, update));
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> CompletableFuture<T> send(Method method) {
        return executeAsync(method);
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> CompletableFuture<T> executeAsync(Method method) {
        try {
            log.info("executed method: {}", method.getClass().getSimpleName());

            if (method instanceof SendMessage parsed) {
                if (parsed.getReplyMarkup() == null)
                    parsed.setReplyMarkup(Keyboard.Reply.DEFAULT);

                log.info(String.format("message sent to {%s} with text {'%s'}",
                        parsed.getChatId(), parsed.getText().replaceAll("\n", " ")));

                return (CompletableFuture<T>) super.executeAsync(parsed);
            } else return super.executeAsync(method);
        } catch (TelegramApiException e) {
            log.warn("Error method execution: {}", e.getMessage());
            return null;
        }
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> CompletableFuture<T> execute(Method method, Session session) {
        if (method instanceof SendMessage) {
            CompletableFuture<Message> message = executeAsync((SendMessage) method);
            message.thenRun(() -> {
                try {
                    session.getBotsMessagesIds().add(message.get().getMessageId());
                } catch (InterruptedException | ExecutionException e) {
                    log.warn(e.getMessage());
                }
            });
            return (CompletableFuture<T>) message;
        } // todo make other Send*

        return executeAsync(method);
    }

    public Session createSession(Bot bot, long chatId) {
        return new Session(bot, chatId) {
            @Override
            protected void init(Bot bot, Update update) {

            }

            @Override
            protected void getNext(Bot bot, Update update) {

            }
        };
    }
}



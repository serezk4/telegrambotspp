package com.serezka.telegram.bot;

import com.serezka.database.model.History;
import com.serezka.database.service.MessageService;
import com.serezka.localization.Localization;
import com.serezka.telegram.api.bots.TelegramLongPollingBot;
import com.serezka.telegram.api.meta.api.objects.Message;
import com.serezka.telegram.api.meta.api.objects.Update;
import com.serezka.telegram.session.Session;
import com.serezka.telegram.util.Keyboard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import com.serezka.telegram.api.meta.api.methods.BotApiMethod;
import com.serezka.telegram.api.meta.api.methods.send.SendMessage;
import com.serezka.telegram.api.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Component
@PropertySource("classpath:telegram.properties")
@Log4j2
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Bot extends TelegramLongPollingBot {
    // bot data
    String botUsername, botToken;

    // handler services
    @NonFinal @Setter
    Handler handler;

    // executor services
    ExecutorServiceRouter executor;

    // localization services
    Localization localization = Localization.getInstance();

    // database services
    MessageService messageService;

    public Bot(@Value("${telegram.bot.username}") String botUsername,
               @Value("${telegram.bot.token}") String botToken,
               @Value("${telegram.bot.threads}") int threadCount,
               MessageService messageService) {
        super(botToken);

        this.botUsername = botUsername;
        this.botToken = botToken;

        this.executor = new ExecutorServiceRouter(threadCount);

        this.messageService = messageService;
    }


    @Override
    public void onUpdateReceived(Update update) {
        log.info("new update received");

        if (executor.isShutdown()) {
            log.info("user {} {} trying to make query", update.getUsername(), update.getChatId());
            execute(SendMessage.builder()
                    .chatId(update).text(localization.get("bot.shutdown"))
                    .build());
            return;
        }

        messageService.save(new History(update.getChatId(), update.getChatId(), update.getQueryType(), update.getText()));
        executor.route(update.getChatId(), () -> handler.process(this, update));
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> T send(Method method) {
        return execute(method);
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) {
        try {
            log.info("executed method: {}", method.getClass().getSimpleName());

            if (method instanceof SendMessage parsed) {
                if (parsed.getReplyMarkup() == null)
                    parsed.setReplyMarkup(Keyboard.Reply.DEFAULT);

                log.info(String.format("message sent to {%s} with text {'%s'}",
                        parsed.getChatId(), parsed.getText().replaceAll("\n", " ")));

                return (T) super.execute(parsed);
            } else return super.execute(method);
        } catch (TelegramApiException e) {
            log.warn("Error method execution: {}", e.getMessage());
            return null;
        }
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method, Session session) {
        if (method instanceof SendMessage) {
            Message message = execute((SendMessage) method);
            session.getBotsMessagesIds().add(message.getMessageId());
            return (T) message;
        } // todo make other Send*

        return execute(method);
    }
}

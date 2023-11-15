package com.serezka.telegram.bot;

import com.serezka.localization.Localization;
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
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@PropertySource("classpath:telegram.properties")
@Log4j2
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Bot extends TelegramLongPollingBot {
    String botUsername, botToken;

    @NonFinal
    @Setter
    Handler handler;

    ExecutorService executor;

    Localization localization = Localization.getInstance();

    public Bot(@Value("${telegram.bot.username}") String botUsername,
               @Value("${telegram.bot.token}") String botToken,
               @Value("${telegram.bot.threads}") int threadCount) {
        super(botToken);

        this.botUsername = botUsername;
        this.botToken = botToken;

        executor = Executors.newFixedThreadPool(threadCount);
    }


    @Override
    public void onUpdateReceived(Update update) {
        Qpdate qpdate = new Qpdate(update);

        log.info("new update");

        if (executor.isShutdown() || executor.isTerminated()) {
            log.info("user {} {} trying to make query", qpdate.getUsername(), qpdate.getChatId());
            execute(SendMessage.builder()
                    .chatId(qpdate.getChatId()).text(localization.get("bot.shutdown"))
                    .parseMode(ParseMode.HTML)
                    .build());
            return;
        }

        executor.submit(() -> {
            try {
                handler.process(this, new Qpdate(update));
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        });
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
}

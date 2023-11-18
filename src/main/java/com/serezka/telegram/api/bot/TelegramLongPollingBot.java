package com.serezka.telegram.api.bot;

import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.util.WebhookUtils;

/**
 * @author Ruben Bermudez
 * @version 1.0+ (finalized by @serezk4)
 * Base abstract class for a bot that will get updates using
 * <a href="https://core.telegram.org/bots/api#getupdates">long-polling</a> method
 */

public abstract class TelegramLongPollingBot extends DefaultAbsSender implements LongPollingBot {
    /**
     * If this is used getBotToken has to be overridden in order to return the bot token!
     * @deprecated Overwriting the getBotToken() method is deprecated. Use the constructor instead
     */
    @Deprecated
    public TelegramLongPollingBot() {
        this(new DefaultBotOptions());
    }

    /**
     * If this is used getBotToken has to be overridden in order to return the bot token!
     * @deprecated Overwriting the getBotToken() method is deprecated. Use the constructor instead
     */
    @Deprecated
    public TelegramLongPollingBot(DefaultBotOptions options) {
        super(options);
    }

    public TelegramLongPollingBot(String botToken) {
        this(new DefaultBotOptions(), botToken);
    }
    public TelegramLongPollingBot(DefaultBotOptions options, String botToken) {
        super(options, botToken);
    }

    @Override
    public void clearWebhook() throws TelegramApiRequestException {
        WebhookUtils.clearWebhook(this);
    }

    @Override
    public void onClosing() {
        exe.shutdown();
    }
}

package com.serezka.telegram.api;

import com.serezka.telegram.api.bot.LongPollingBot;
import org.telegram.telegrambots.meta.generics.BotOptions;

public interface BotSession {
    void setOptions(BotOptions options);
    void setToken(String token);
    void setCallback(LongPollingBot callback);

    /**
     * Starts the bot
     */
    void start();

    /**
     * Stops the bot
     */
    void stop();

    /**
     * Check if the bot is running
     * @return True if the bot is running, false otherwise
     */
    boolean isRunning();
}


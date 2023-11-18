package com.serezka.telegram.api.bot;


import com.serezka.telegram.api.update.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotOptions;
import org.telegram.telegrambots.meta.generics.TelegramBot;

import java.util.List;

/**
 * @author Ruben Bermudez
 * @version 1.0+ (finalized by @serezk4)
 * @brief Callback to handle updates.
 * @date 20 of June of 2015
 */
public interface LongPollingBot extends TelegramBot {
    void onUpdateReceived(Update update);

    default void onUpdatesReceived(List<Update> updates) {
        updates.forEach(this::onUpdateReceived);
    }

    BotOptions getOptions();

    void clearWebhook() throws TelegramApiRequestException;

    default void onClosing() {}
}

package com.serezka.telegram.api.meta.generics;

import com.serezka.telegram.api.meta.api.methods.BotApiMethod;
import com.serezka.telegram.api.meta.api.methods.updates.SetWebhook;
import com.serezka.telegram.api.meta.api.objects.Update;
import com.serezka.telegram.api.meta.exceptions.TelegramApiException;
import com.serezka.telegram.api.meta.exceptions.TelegramApiRequestException;
import com.serezka.telegram.api.meta.generics.TelegramBot;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * Callback to handle updates.
 */
public interface WebhookBot extends TelegramBot {
    /**
     * This method is called when receiving updates via webhook
     * @param update Update received
     */
    BotApiMethod<?> onWebhookUpdateReceived(Update update);

    /**
     * Execute setWebhook method to set up the url of the webhook
     * @throws TelegramApiRequestException In case of error executing the request
     */
    void setWebhook(SetWebhook setWebhook) throws TelegramApiException;

    /**
     * Gets in the url for the webhook
     * @return path in the url
     */
    String getBotPath();
}

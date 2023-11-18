package com.serezka.telegram.api.meta.generics;

import com.serezka.telegram.api.meta.exceptions.TelegramApiException;
import com.serezka.telegram.api.meta.generics.WebhookBot;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * Webhook interface
 */
public interface Webhook {
    void startServer() throws TelegramApiException;
    void registerWebhook(WebhookBot callback);
    void setInternalUrl(String internalUrl);
    void setKeyStore(String keyStore, String keyStorePassword) throws TelegramApiException;
}

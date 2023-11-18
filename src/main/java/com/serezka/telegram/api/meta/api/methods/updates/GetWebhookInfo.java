package com.serezka.telegram.api.meta.api.methods.updates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.serezka.telegram.api.meta.api.methods.BotApiMethod;
import com.serezka.telegram.api.meta.api.objects.WebhookInfo;
import com.serezka.telegram.api.meta.exceptions.TelegramApiRequestException;

/**
 * @author Ruben Bermudez
 * @version 2.4
 * Use this method to get current webhook status.
 * Requires no parameters.
 * On success, returns a WebhookInfo object.
 * Will throw an error, if the bot is using getUpdates.
 */
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class GetWebhookInfo extends BotApiMethod<WebhookInfo> {
    public static final String PATH = "getwebhookinfo";

    @Override
    public String getMethod() {
        return PATH;
    }


    @Override
    public WebhookInfo deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, WebhookInfo.class);
    }
}

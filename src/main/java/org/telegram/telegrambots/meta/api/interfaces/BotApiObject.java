package org.telegram.telegrambots.meta.api.interfaces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.session.Session;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * An object from the Bots API received from Telegram Servers
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface BotApiObject extends Serializable {
    default Session createSession() {
        return new Session() {
            @Override
            protected void init(Bot bot, Update update) {

            }

            @Override
            protected void getNext(Bot bot, Update update) {

            }
        };
    }
}

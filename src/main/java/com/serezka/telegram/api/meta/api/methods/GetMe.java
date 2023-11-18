package com.serezka.telegram.api.meta.api.methods;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.serezka.telegram.api.meta.api.methods.BotApiMethod;
import com.serezka.telegram.api.meta.api.objects.User;
import com.serezka.telegram.api.meta.exceptions.TelegramApiRequestException;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * A simple method for testing your bot's auth token. Requires no parameters.
 * Returns basic information about the bot in form of a User object
 */
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class GetMe extends BotApiMethod<User> {
    public static final String PATH = "getme";

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public User deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, User.class);
    }
}

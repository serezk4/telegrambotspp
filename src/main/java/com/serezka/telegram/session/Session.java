package com.serezka.telegram.session;

import com.serezka.telegram.api.meta.api.methods.BotApiMethod;
import com.serezka.telegram.api.meta.api.methods.send.SendMessage;
import com.serezka.telegram.api.meta.api.objects.Message;
import com.serezka.telegram.api.meta.api.objects.Update;
import com.serezka.telegram.bot.Bot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter @Log4j2
public abstract class Session {
    private static int idCounter = 0;

    // init data
    private final Queue<Integer> botsMessagesIds = new PriorityQueue<>();
    private final List<Integer> usersMessagesIds = new ArrayList<>();
    final long id = idCounter++;
    @Setter boolean saveUsersMessages = true;

    private final List<String> history = new LinkedList<>();
    private boolean created;

    public void next(Bot bot, Update update) {
        usersMessagesIds.add(update.getMessageId());

        if (!created) {
            init(bot, update);
            created = true;
        } else getNext(bot, update);
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Bot bot, Method method) {
        if (method instanceof SendMessage sendMessage) {
            Message result = bot.execute(sendMessage);
            botsMessagesIds.add(result.getMessageId());

            return (T) result;
        }

        return bot.execute(method);
    }


    protected abstract void init(Bot bot, Update update);
    protected abstract void getNext(Bot bot, Update update);
    protected abstract void destroy(Bot bot, Update update);
}

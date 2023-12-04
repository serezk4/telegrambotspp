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

import java.io.Serializable;
import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public abstract class Session {
    private static int idCounter = 0;

    // init data
    final Queue<Integer> botsMessagesIds = new PriorityQueue<>();
    final List<Integer> usersMessagesIds = new ArrayList<>();
    final long id = idCounter++;
    @Getter
    @Setter
    boolean saveUsersMessages = true;

    private final List<String> history = new LinkedList<>();
    private boolean created;

    public void getNext(Bot bot, Update update) {
        usersMessagesIds.add(update.getMessageId());

        if (!created) {
            init(bot, update);
            created = true;
        } else next(bot, update);
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Bot bot,Method method) {
        if (method instanceof SendMessage sendMessage) {
            Message result = bot.execute(sendMessage);
            botsMessagesIds.add(result.getMessageId());

            return (T) result;
        }

        return bot.execute(method);
    }


    protected abstract void init(Bot bot, Update update);
    protected abstract void next(Bot bot, Update update);
    protected abstract void destroy(Bot bot, Update update);
}

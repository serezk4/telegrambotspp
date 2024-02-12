package com.serezka.telegram.session.empty;

import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.command.Command;
import com.serezka.telegram.session.Session;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EmptySession extends Session {
    Command command;

    @Override
    protected void init(Bot bot, Update update) {
        getNext(bot, update);
        destroy(bot, update);
    }

    @Override
    protected void getNext(Bot bot, Update update) {
        command.execute(bot, update);
    }
}

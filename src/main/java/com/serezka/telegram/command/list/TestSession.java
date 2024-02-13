package com.serezka.telegram.command.list;

import com.serezka.database.model.DUser;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.command.Command;
import com.serezka.telegram.util.Keyboard;
import org.apache.commons.lang3.tuple.Pair;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * Test session
 * @version 1.0
 */
public class TestSession extends Command {
    public TestSession() {
        super(List.of("test"), "test", DUser.Role.MIN);
    }

    @Override
    public void execute(Bot bot, Update update) {
        bot.createSession(bot, update.getChatId())
                .send(upd -> Pair.of("test", Keyboard.Reply.DEFAULT))
                .send(upd -> Pair.of(upd.getText(), Keyboard.Reply.DEFAULT));

//        bot.createSession(bot, update.getChatId())
//                .send("").get("age:")
//                .send("ok").execute();
    }
}

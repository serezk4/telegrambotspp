package com.serezka.telegram.command.list;

import com.serezka.database.model.DUser;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.command.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
                .send((b, u, s) -> b.send(SendMessage.builder().chatId(u.getChatId()).text("age:").build()))
                .menu(/*todo*/)
                .send("Test")
                .get("test?:")
                .rollbackIf((b, u) -> true).execute((b, u, s) -> b.send(SendMessage.builder().chatId(u.getChatId()).text("ok").build()));

//        bot.createSession(bot, update.getChatId())
//                .send("").get("age:")
//                .send("ok").execute();
    }
}

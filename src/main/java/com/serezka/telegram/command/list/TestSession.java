package com.serezka.telegram.command.list;

import com.serezka.database.model.DUser;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.command.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public class TestSession extends Command {
    public TestSession() {
        super(List.of("test"), "test", DUser.Role.MIN);
    }

    @Override
    public void execute(Bot bot, Update update) {
        bot.createSession(bot, update.getChatId());

//        bot.execute(SendMessage.builder().text("test").chatId(update).build())
//                .createSession()
//                .repeat(bot.execute(SendMessage.builder().build()), "regex")
//                .after(); // todo
    }
}

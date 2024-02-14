package com.serezka.telegram.command.list;

import com.serezka.database.model.DUser;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.command.Command;
import com.serezka.telegram.session.SessionConfiguration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class Profile extends Command {
    public Profile() {
        super(List.of("/profile"), "command.profile", DUser.Role.MIN);
    }

    @Override
    public void execute(Bot bot, Update update) {
        bot.createSession(SessionConfiguration.create()
                        .saveBotsMessages(false)
                        .saveUsersMessages(false)
                        .canEditMessages(true)
                .execute((b, u, s) -> s.send(SendMessage.builder()
                        .chatId(u).text("test!").build()))
                .get("test", null)
                .execute((b, u, s) -> s.send(SendMessage.builder()
                        .chatId(u).text("qwe").build())), bot, update);
    }
}

package com.serezka.telegram.command.list;

import com.serezka.database.model.DUser;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.command.Command;
import com.serezka.telegram.session.SessionConfiguration;
import org.springframework.stereotype.Component;
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
                .execute((b, u, s) -> s.send("test1!"))
                .execute((b, u, s) -> s.send("test2!"))
                .execute((b, u, s) -> s.send("qwe bro"))
                        .execute((b,u,s)->s.send("test3)")),

                bot, update);
    }
}

package com.serezka.telegram.command.list;

import com.serezka.database.model.DUser;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.command.Command;
import com.serezka.telegram.session.step.StepSessionConfiguration;
import com.serezka.telegram.util.Keyboard;
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
        bot.createSession(StepSessionConfiguration.create()
                        .saveBotsMessages(true)
                        .saveUsersMessages(false)
                        .canEditMessages(true)
                        .execute((s,u) -> s.send("*SIGMA REGISTRATION*\n*name*: ..."))
                        .execute((s,u) -> s.append(u.getText() + "\n*password*: ..."))
                        .execute((s,u) -> s.append(u.getText() + "\n*result*: ..."))
                        .execute((s,u) -> s.append(u.getText(), Keyboard.Inline.getResizableKeyboard(List.of(new Keyboard.Inline.Button("confirm", List.of("123"), 2)), 1)))
                        .execute((s, u) -> s.send("Вы успешно зарегались [" + s.getData() + "]")),
                bot, update);
    }
}

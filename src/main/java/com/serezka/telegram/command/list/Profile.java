package com.serezka.telegram.command.list;

import com.serezka.database.model.DUser;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.command.Command;
import com.serezka.telegram.session.menu.MenuButton;
import com.serezka.telegram.session.menu.MenuSessionConfiguration;
import com.serezka.telegram.session.step.StepSessionConfiguration;
import com.serezka.telegram.util.keyboard.Keyboard;
import com.serezka.telegram.util.keyboard.type.Inline;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;

@Component
public class Profile extends Command {
    public Profile() {
        super(List.of("/profile"), "command.profile", DUser.Role.MIN);
    }

    @Override
    public void execute(Bot bot, Update update) {
        bot.createMenuSession(MenuSessionConfiguration.create()
                .alloc("test1", (data, session) -> Triple.of("hello world!", Collections.singletonList(Collections.singletonList(new MenuButton("info", "info"))), 2))
                .alloc("info", (d, s) -> Triple.of("bro)", Collections.singletonList(Collections.singletonList(new MenuButton("info2", "test1"))), 2))
                .onInit("test1"), bot, update);

        bot.createStepSession(StepSessionConfiguration.create()
                        .saveBotsMessages(true)
                        .saveUsersMessages(false)
                        .canEditMessages(true)
                        .execute((s, u) -> s.send("*SIGMA REGISTRATION*\n*name*: ..."))
                        .execute((s, u) -> s.append(u.getText() + "\n*password*: ..."))
                        .execute((s, u) -> s.append(u.getText() + "\n*result*: ..."))
                        .execute((s, u) -> s.append(u.getText(), Inline.getResizableKeyboard(List.of(new Inline.Button("confirm", List.of("123"), 2)), 1)))
                , bot, update);
    }
}

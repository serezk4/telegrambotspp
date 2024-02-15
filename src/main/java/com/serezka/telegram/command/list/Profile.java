package com.serezka.telegram.command.list;

import com.serezka.database.model.DUser;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.command.Command;
import com.serezka.telegram.session.menu.MenuSessionConfiguration;
import com.serezka.telegram.session.menu.PageResponse;
import com.serezka.telegram.session.step.StepSessionConfiguration;
import com.serezka.telegram.util.keyboard.type.Inline;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackBundle;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class Profile extends Command {
    public Profile() {
        super(List.of("/profile"), "command.profile", DUser.Role.MIN);
    }

    @Override
    public void execute(Bot bot, Update update) {

        bot.createMenuSession(MenuSessionConfiguration.create()
                        .alloc("info", (data, session) -> new PageResponse("test", List.of(), 2))
                        .alloc("info2", (d, s) -> new PageResponse("test1", List.of(), 4))
                        .onInit("info"),


                bot, update);

        bot.createStepSession(StepSessionConfiguration.create()
                        .saveBotsMessages(true)
                        .saveUsersMessages(false)
                        .canEditMessages(true)
                        .execute((s, u) -> s.send("*SIGMA REGISTRATION*\n*name*: ..."))
                        .execute((s, u) -> s.append(u.getText() + "\n*password*: ..."))
                        .execute((s, u) -> s.append(u.getText() + "\n*result*: ..."))
//                        .execute((s, u) -> s.append(u.getText(), Inline.getResizableKeyboard(List.of(new Inline.Button("confirm", new CallbackBundle(), 2)), 1)))
                , bot, update);
    }
}

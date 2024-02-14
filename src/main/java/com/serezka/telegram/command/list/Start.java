package com.serezka.telegram.command.list;

import com.serezka.database.model.DUser;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.command.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class Start extends Command {
    public Start() {
        super(List.of("/start"), "load bot", DUser.Role.MIN);
    }

    @Override
    public void execute(Bot bot, Update update) {
        bot.send(SendMessage.builder()
                .chatId(update).text("hello!")
                .build());
    }
}

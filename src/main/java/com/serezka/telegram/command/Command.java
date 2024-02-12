package com.serezka.telegram.command;

import com.serezka.database.model.DUser;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.session.Session;
import com.serezka.telegram.session.empty.EmptySession;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public abstract class Command {
    @NonFinal
    Session session;

    {
        // initialize session
        createSession();
    }

    List<String> usage;
    String help;
    DUser.Role requiredRole;

    public Command(List<String> usage, String help, DUser.Role requiredRole) {
        this.usage = usage;
        this.help = help;
        this.requiredRole = requiredRole;
    }

    public Command(List<String> usage, DUser.Role requiredRole) {
        this.usage = usage;
        this.help = "no help provided";
        this.requiredRole = requiredRole;
    }

    public Command(List<String> usage) {
        this.usage = usage;
        this.help = "no help provided";
        this.requiredRole = DUser.Role.MAX;
    }

    public abstract void execute(Bot bot, Update update);
    public void createSession() {
        this.session = new EmptySession(this);
    }
}

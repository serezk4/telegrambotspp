package com.serezka.telegram.command;

import com.serezka.database.model.User;
import com.serezka.telegram.api.update.Update;
import com.serezka.telegram.bot.Bot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public abstract class Command {
    // session todo

    List<String> usage;
    String help;
    User.Role requiredRole;

    public Command(List<String> usage, String help, User.Role requiredRole) {
        this.usage = usage;
        this.help = help;
        this.requiredRole = requiredRole;
    }

    public Command(List<String> usage, User.Role requiredRole) {
        this.usage = usage;
        this.help = "no help provided";
        this.requiredRole = requiredRole;
    }

    public Command(List<String> usage) {
        this.usage = usage;
        this.help = "no help provided";
        this.requiredRole = User.Role.MAX;
    }

    public abstract void execute(Bot bot, Update update, User user);
    public void createSession() {
        // todo make create empty session
    }
}

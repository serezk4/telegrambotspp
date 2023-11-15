package com.serezka.telegram.command;

import com.serezka.database.model.User;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.bot.Qpdate;

import java.util.List;

public abstract class Command {
    // session todo

    List<String> usage;
    String help;
    User.Role requiredRole;

    public abstract void execute(Bot bot, Qpdate update, User user);
    public void createSession() {
        // todo make create empty session
    }
}

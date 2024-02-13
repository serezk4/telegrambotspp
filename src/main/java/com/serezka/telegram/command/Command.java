package com.serezka.telegram.command;

import com.serezka.database.model.DUser;
import com.serezka.telegram.bot.Bot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * Abstract class for commands
 * Create your own command by extending this class
 * @version 1.0.1
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public abstract class Command {
    List<String> usage;
    String help;
    DUser.Role requiredRole;

    public Command(List<String> usage, String help, DUser.Role requiredRole) {
        this.usage = usage;
        this.help = help;
        this.requiredRole = requiredRole;
    }

    public Command(List<String> usage, DUser.Role requiredRole) {
        this(usage, "[x]", requiredRole);
    }

    public Command(List<String> usage) {
        this(usage, DUser.Role.MAX);
    }

    public abstract void execute(Bot bot, Update update);
}

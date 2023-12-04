package com.serezka.telegram.bot;

import com.serezka.database.model.User;
import com.serezka.database.service.UserService;
import com.serezka.localization.Localization;
import com.serezka.telegram.api.meta.api.methods.send.SendMessage;
import com.serezka.telegram.api.meta.api.objects.Update;
import com.serezka.telegram.command.Command;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PropertySource("classpath:telegram.properties")
public class Handler {
    // handler per-init settings
    @Getter
    List<Command> commands = new ArrayList<>();

    public void addCommand(Command command) {
        commands.add(command);
    }

    // database services
    UserService userService;

    // localization
    Localization localization = Localization.getInstance();

    // cache
    Set<Long> authorized = Collections.newSetFromMap(new WeakHashMap<>());

    public void process(Bot bot, Update update) {
        if (!authorized.contains(update.getChatId()))
            checkAuth(bot, update);

        // get user
        final User user = getUser(bot, update);
        if (user == null) return;

        update.setDatabaseUser(user);

        // validate query
        if (!Settings.availableQueryTypes.contains(update.getQueryType())) {
            bot.send(SendMessage.builder()
                    .chatId(update).text(localization.get("handler.query.type_error", user))
                    .build());
            return;
        }

        bot.send(SendMessage.builder()
                .chatId(update).text("test")
                .replyToMessageId(update).build());

        // check session

        // get command

        // execute
    }

    private User getUser(Bot bot, Update update) {
        Optional<User> optionalUser = userService.findByChatId(update.getChatId());

        if (optionalUser.isEmpty()) {
            log.warn("User exception (can't find or create) | {} : {}", update.getUsername(), update.getChatId());
            bot.execute(SendMessage.builder()
                    .chatId(update).text(localization.get("handler.database.error"))
                    .build());
            return null;
        }

        authorized.add(update.getChatId());
        return optionalUser.get();
    }

    private void checkAuth(Bot bot, Update update) {
        if (!userService.existsByChatIdOrUsername(update.getChatId(), update.getUsername()))
            userService.save(new User(update.getChatId(), update.getUsername()));
    }

    public String getHelp(User user) {
        return localization.get("help.title", user) + "\n" + commands.stream()
                .filter(command -> command.getRequiredRole().getAdminLvl() <= user.getRole().getAdminLvl())
                .map(command -> String.format(localization.get("help.command"), command.getUsage(), command.getHelp()))
                .collect(Collectors.joining());
    }
}

package com.serezka.telegram.bot;

import com.serezka.database.model.User;
import com.serezka.database.service.UserService;
import com.serezka.localization.Localization;
import com.serezka.telegram.api.methods.SendMessage;
import com.serezka.telegram.api.update.Update;
import com.serezka.telegram.command.Command;
import com.serezka.telegram.util.AntiSpam;
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

    // anti-spam services
    AntiSpam antiSpam;

    // localization
    Localization localization = Localization.getInstance();

    // cache
    Set<Long> authorized = Collections.newSetFromMap(new WeakHashMap<>());

    public void process(Bot bot, Update update) {
        // check if user exists in database
        if (!authorized.contains(update.getChatId()))
            checkAuth(bot, update);

        // get user
        final User user = getUser(bot, update.getChatId(), update.getUsername());

        // validate query
        if (!Settings.availableQueryTypes.contains(update.getQueryType())) {
            // todo or just ignore
            bot.execute(SendMessage.builder()
                    .chatId(update).text(localization.get("handler.query.type_error", user.getLocalization()))
                    .build());
            return;
        }

        // check session

        // get command

        // execute
    }

    private User getUser(Bot bot, long chatId, String username) {
        Optional<User> optionalUser = userService.findByChatId(chatId);

        if (optionalUser.isEmpty()) {
            log.warn("User exception (can't find or create) | {} : {}", username, chatId);
            bot.execute(SendMessage.builder()
                    .chatId(chatId).text(localization.get("handler.database.error"))
                    .build());
            return null;
        }

        authorized.add(chatId);
        return optionalUser.get();
    }

    private void checkAuth(Bot bot, Update update) {
        if (!userService.existsByChatIdOrUsername(update.getChatId(), update.getUsername()))
            userService.save(new User(update.getChatId(), update.getUsername()));
    }

    public String getHelp(User.Role userRole) {
        StringBuilder help = new StringBuilder("Кажется, вы ошиблись в команде. Список допустимых команд:\n");
        help.append(commands.stream()
                .filter(command -> command.getRequiredRole().getAdminLvl() <= userRole.getAdminLvl())
                .map(command -> String.format(" - <b>%s</b> - %s%n", command.getUsage(), command.getHelp()))
                .collect(Collectors.joining()));
        return help.toString();
    }
}

package com.serezka.telegram.bot;

import com.serezka.database.model.DUser;
import com.serezka.database.service.UserService;
import com.serezka.localization.Localization;
import com.serezka.telegram.command.Command;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Main class for update handling
 * @version 1.0
 */
@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PropertySource("classpath:telegram.properties")
public class Handler {
    @Getter List<Command> commands;

    // entities
    UserService userService;

    // localization
    Localization localization = Localization.getInstance();

    // cache
    Set<Long> authorized = Collections.newSetFromMap(new WeakHashMap<>());

    /**
     * proceed the update and handling it
     * @param bot - self
     * @param update - update from client
     */
    public void process(Bot bot, Update update) {
        if (!authorized.contains(update.getChatId()))
            checkAuth(update);

        // get user
        final DUser duser = getUser(bot, update);
        if (duser == null) return;

        update.setDatabaseUser(duser);

        // validate query
        if (!Settings.availableQueryTypes.contains(update.getQueryType())) {
            bot.send(SendMessage.builder()
                    .chatId(update).text(localization.get("handler.query.type_error", duser))
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

    /**
     * Get user from database
     * @param bot - self
     * @param update - update from client
     * @return user from database
     */
    private DUser getUser(Bot bot, Update update) {
        Optional<DUser> optionalUser = userService.findByChatId(update.getChatId());

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

    /**
     * Check user in database
     * @param update
     */
    private void checkAuth(Update update) {
        if (!userService.existsByChatIdOrUsername(update.getChatId(), update.getUsername()))
            userService.save(new DUser(update.getChatId(), update.getUsername()));
    }

    /**
     * Get help for user by his role
     * @param DUser - user from database
     * @return help message
     */
    public String getHelp(DUser DUser) {
        return localization.get("help.title", DUser) + "\n" + commands.stream()
                .filter(command -> command.getRequiredRole().getAdminLvl() <= DUser.getRole().getAdminLvl())
                .map(command -> String.format(localization.get("help.command"), command.getUsage(), command.getHelp()))
                .collect(Collectors.joining());
    }
}

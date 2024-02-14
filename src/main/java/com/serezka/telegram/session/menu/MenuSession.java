package com.serezka.telegram.session.menu;

import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.util.Keyboard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

/**
 * Menu session
 *
 * @version 1.0
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class MenuSession {
    static int idCounter = 0;

    // configuration
    MenuSessionConfiguration configuration;

    // chat data
    Bot bot;
    long chatId;

    // runtime data
    Deque<Message> botMessages = new ArrayDeque<>();
    long id = UUID.randomUUID().hashCode();

    // cache
    @NonFinal
    Map<Long, Menu> cachedMenus;

    public MenuSession(MenuSessionConfiguration configuration, Bot bot, long chatId) {
        this.configuration = configuration;
        this.bot = bot;
        this.chatId = chatId;
    }

    public void next(Bot bot, Update update) {
        if (!update.hasCallbackQuery()) {
            log.warn("update has no callback query");
            return;
        }

        final String callback = update.getCallbackQuery().getData();
        final String[] args = callback.split("\\" + Keyboard.Delimiter.SERVICE, 3);

        if (args.length < 2) {
            log.warn("callback has no service delimiter, can't parse button ID");
            return;
        }

        if (!args[0].matches("\\d+") || !args[1].matches("\\d+")) {
            log.warn("callback has no valid button or session ID {}", Arrays.toString(args));
            return;
        }

        Menu selected = cachedMenus.get(Long.parseLong(args[1]));
        List<String> menuArgs = args.length > 2 ? Arrays.stream(args[2].split("\\" + Keyboard.Delimiter.DATA)).toList() : null;

        Pair<String, List<List<MenuButton>>> result = selected.apply(menuArgs, this);

        // map buttons
        result.getRight().forEach(row -> row.forEach(button -> button.setSessionId(id)));

        // send todo
    }
}

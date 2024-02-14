package com.serezka.telegram.session.menu;

import com.serezka.telegram.bot.Bot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Menu session
 * @version 1.0
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class MenuSession {
    static int idCounter = 0;

    MenuSessionConfiguration configuration;

    Bot bot;
    long chatId;

    Deque<Message> botMessages = new ArrayDeque<>();

    long id = idCounter++;

    public MenuSession(MenuSessionConfiguration configuration, Bot bot, long chatId) {
        this.configuration = configuration;
        this.bot = bot;
        this.chatId = chatId;
    }


}

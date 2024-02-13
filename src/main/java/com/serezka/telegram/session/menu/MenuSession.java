package com.serezka.telegram.session.menu;

import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.session.Session;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MenuSession extends Session {
    public MenuSession(Bot bot, long chatId) {
        super(bot, chatId);
    }

    @Override
    protected void init(Bot bot, Update update) {

    }

    @Override
    protected void getNext(Bot bot, Update update) {

    }

    @Override
    protected void destroy(Bot bot, Update update) {

    }
}

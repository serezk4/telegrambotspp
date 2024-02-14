package com.serezka.telegram.session;

import com.serezka.telegram.bot.Bot;
import org.apache.commons.lang3.function.TriConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Step extends TriConsumer<Bot, Update, Session> {
}

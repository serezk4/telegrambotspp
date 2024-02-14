package com.serezka.telegram.session.menu;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.BiConsumer;

public interface Menu extends BiConsumer<Update, MenuSession> {
}

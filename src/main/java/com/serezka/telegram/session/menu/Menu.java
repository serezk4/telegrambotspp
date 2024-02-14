package com.serezka.telegram.session.menu;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.function.BiFunction;

public interface Menu extends BiFunction<List<String>, MenuSession, Pair<String, List<List<MenuButton>>>> {}

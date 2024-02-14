package com.serezka.telegram.session.menu;

import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.function.BiFunction;

public interface Menu extends BiFunction<List<String>, MenuSession, Triple<String, List<List<MenuButton>>, Integer>> {}

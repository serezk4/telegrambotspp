package com.serezka.telegram.session.menu;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class MenuSessionManager {
    private static final Map<Long, List<MenuSession>> menuSessions = new HashMap<>();

    public static boolean containsSession(long chatId) {
        if (!menuSessions.containsKey(chatId)) return false;

        log.info("Checking session in chat " + chatId);

        synchronized (menuSessions.get(chatId)) {
            return !menuSessions.get(chatId).isEmpty();
        }
    }

    public static List<MenuSession> getSession(long chatId) {
        if (!containsSession(chatId)) return null;

        log.info("Getting session from chat " + chatId);

        synchronized (menuSessions.get(chatId)) {
            return menuSessions.get(chatId);
        }
    }

    public static void addSession(long chatId, MenuSession menuSession) {
        if (!containsSession(chatId)) menuSessions.put(chatId, List.of());

        log.info("Adding session " + menuSession.getId() + " to chat " + chatId);

        synchronized (menuSessions.get(chatId)) {
            menuSessions.get(chatId).add(menuSession);
        }
    }

    public static void removeSession(long chatId, MenuSession menuSession) {
        if (!containsSession(chatId)) return;

        log.info("Removing session " + menuSession.getId() + " from chat " + chatId);

        synchronized (menuSessions.get(chatId)) {
            menuSessions.get(chatId).remove(menuSession);
        }
    }

    public static void removeSession(long chatId) {
        if (!containsSession(chatId)) return;

        log.info("Removing session from chat " + chatId);

        synchronized (menuSessions.get(chatId)) {
            menuSessions.get(chatId).clear();
        }
    }
}

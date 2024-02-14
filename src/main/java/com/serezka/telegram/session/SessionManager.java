package com.serezka.telegram.session;

import lombok.extern.log4j.Log4j2;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Log4j2
public class SessionManager {
    private static final Map<Long, Deque<Session>> sessions = new HashMap<>();

    public static boolean containsSession(long chatId) {
        if (!sessions.containsKey(chatId)) return false;

        log.info("Checking session in chat " + chatId);

        synchronized (sessions.get(chatId)) {
            return sessions.containsKey(chatId);
        }
    }

    public static Session getSession(long chatId) {
        if (!containsSession(chatId)) return null;

        log.info("Getting session from chat " + chatId);

        synchronized (sessions.get(chatId)) {
            return sessions.get(chatId).peekLast();
        }
    }

    public static void addSession(long chatId, Session session) {
        if (!containsSession(chatId)) sessions.put(chatId, new LinkedList<>());

        log.info("Adding session " + session.getId() + " to chat " + chatId);

        synchronized (sessions.get(chatId)) {
            sessions.get(chatId).addLast(session);
        }
    }

    public static void removeSession(long chatId, Session session) {
        if (!containsSession(chatId)) return;

        log.info("Removing session " + session.getId() + " from chat " + chatId);

        synchronized (sessions.get(chatId)) {
            sessions.get(chatId).remove(session);
        }
    }

    public static void removeSession(long chatId) {
        if (!containsSession(chatId)) return;

        log.info("Removing session from chat " + chatId);

        synchronized (sessions.get(chatId)) {
            sessions.get(chatId).pollLast();
        }
    }
}

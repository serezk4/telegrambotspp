package com.serezka.telegram.session;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static final Map<Long, Deque<Session>> sessions = new HashMap<>();

    public static boolean containsSession(long chatId) {
        synchronized (sessions.get(chatId)) {
            return sessions.containsKey(chatId);
        }
    }

    public static Session getSession(long chatId) {
        synchronized (sessions.get(chatId)) {
            return sessions.get(chatId).peekLast();
        }
    }

    public static void addSession(long chatId, Session session) {
        synchronized (sessions.get(chatId)) {
            sessions.get(chatId).addLast(session);
        }
    }

    public static void removeSession(long chatId, Session session) {
        synchronized (sessions.get(chatId)) {
            sessions.get(chatId).remove(session);
        }
    }

    public static void removeSession(long chatId) {
        synchronized (sessions.get(chatId)) {
            sessions.get(chatId).pollLast();
        }
    }
}

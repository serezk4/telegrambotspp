package com.serezka.telegram.session;

import com.serezka.telegram.api.update.Update;
import com.serezka.telegram.bot.Bot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public abstract class Session {
    private static int idCounter = 0;

    // init data
    final Queue<Integer> botsMessagesIds = new PriorityQueue<>();
    final List<Integer> usersMessagesIds = new ArrayList<>();
    final long id = idCounter++;
    @Getter @Setter boolean saveUsersMessages = true;

    private final List<String> history = new LinkedList<>();
    private boolean created;

    // generate answer
    public void next(Bot bot, Update update){};
    public void destroy(Bot bot, Update update){};
}
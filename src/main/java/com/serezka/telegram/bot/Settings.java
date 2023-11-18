package com.serezka.telegram.bot;

import com.serezka.telegram.api.update.Update;

import java.util.List;

public class Settings {
    public static final List<Update.QueryType> availableQueryTypes = List.of(Update.QueryType.MESSAGE, Update.QueryType.CALLBACK_QUERY, Update.QueryType.INLINE_QUERY);
}

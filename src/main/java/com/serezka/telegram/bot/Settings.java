package com.serezka.telegram.bot;

import java.util.List;

public class Settings {
    public static final List<Qpdate.QueryType> availableQueryTypes = List.of(Qpdate.QueryType.MESSAGE, Qpdate.QueryType.CALLBACK_QUERY, Qpdate.QueryType.INLINE_QUERY);
}

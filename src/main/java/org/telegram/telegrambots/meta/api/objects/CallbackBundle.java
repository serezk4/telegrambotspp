package org.telegram.telegrambots.meta.api.objects;

import java.util.Collections;
import java.util.List;

public record CallbackBundle(List<String> info, List<String> data) {
    public static CallbackBundle empty() {
        return new CallbackBundle(Collections.emptyList(), Collections.emptyList());
    }
}

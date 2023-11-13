package com.serezka.telegram.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * anti-spam system to refuse critical bot load
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component @Log4j2
@PropertySource("classpath:telegram.properties")
public class AntiSpam {
    Long duration;

    private AntiSpam(@Value("${services.anti_spam.duration}") Long duration) {
        this.duration = duration;
    }

    Map<Long, Long> usersLastMessage = new WeakHashMap<>();

    public boolean isSpam(long userId) {
        // get last and current time
        long lastMessageTime = usersLastMessage.getOrDefault(userId, 0L);
        long currentTime = System.currentTimeMillis();

        // update last message's time
        usersLastMessage.put(userId, currentTime);

        // check is within allowed range
        return isWithinAllowedRange(currentTime, lastMessageTime);
    }

    private boolean isWithinAllowedRange(long currentTime, long lastMessageTime) {
        return (currentTime - lastMessageTime) <= duration * 1000;
    }
}

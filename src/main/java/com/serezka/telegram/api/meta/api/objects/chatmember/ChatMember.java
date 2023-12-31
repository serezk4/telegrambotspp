package com.serezka.telegram.api.meta.api.objects.chatmember;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.serezka.telegram.api.meta.api.interfaces.BotApiObject;
import com.serezka.telegram.api.meta.api.objects.User;
import com.serezka.telegram.api.meta.api.objects.chatmember.serialization.ChatMemberDeserializer;

/**
 * @author Ruben Bermudez
 * @version 5.3
 *
 * This object contains information about one member of a chat. Currently, the following 6
 * types of chat members are supported:
 */
@JsonDeserialize(using = ChatMemberDeserializer.class)
public interface ChatMember extends BotApiObject {
    String getStatus();
    User getUser();
}

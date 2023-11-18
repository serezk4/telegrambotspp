package com.serezka.telegram.api.meta.api.objects.videochat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.serezka.telegram.api.meta.api.interfaces.BotApiObject;
import com.serezka.telegram.api.meta.api.objects.User;

import java.util.List;

/**
 * This object represents a service message about new members invited to a video chat.
 * @author Ruben Bermudez
 * @version 6.0
 */
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VideoChatParticipantsInvited implements BotApiObject {
    private static final String USERS_FIELD = "users";

    /**
     * Optional.
     * New members that were invited to the voice chat
     */
    @JsonProperty(USERS_FIELD)
    private List<User> users;
}

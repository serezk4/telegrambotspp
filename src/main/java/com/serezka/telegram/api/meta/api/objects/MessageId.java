package com.serezka.telegram.api.meta.api.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.serezka.telegram.api.meta.api.interfaces.BotApiObject;

/**
 * This object represents a unique message identifier.
 * @author Ruben Bermudez
 * @version 5.0
 */
@SuppressWarnings("WeakerAccess")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageId implements BotApiObject {

    private static final String MESSAGEID_FIELD = "message_id";

    /**
     * Unique message identifier
     */
    @JsonProperty(MESSAGEID_FIELD)
    private Long messageId;

}

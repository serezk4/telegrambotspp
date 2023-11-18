package com.serezka.telegram.api.meta.api.objects.description;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.serezka.telegram.api.meta.api.interfaces.BotApiObject;

/**
 * @author Ruben Bermudez
 * @version 6.6
 * This object represents the bot's description.
 */
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class BotDescription implements BotApiObject  {
    private static final String DESCRIPTION_FIELD = "description";

    /**
     * The bot's description
     */
    @JsonProperty(DESCRIPTION_FIELD)
    private String description;
}

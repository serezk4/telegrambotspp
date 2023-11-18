package com.serezka.telegram.api.meta.api.objects.menubutton;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import com.serezka.telegram.api.meta.api.objects.menubutton.MenuButton;
import com.serezka.telegram.api.meta.exceptions.TelegramApiValidationException;

/**
 * @author Ruben Bermudez
 * @version 6.0
 *
 * Represents menu button, which opens the list of bot's commands.
 */
@SuppressWarnings({"unused"})
@JsonDeserialize
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString
@Builder
public class MenuButtonCommands extends MenuButton {
    private static final String TYPE = "commands"; ///< Type of the button, must be commands

    @Override
    public void validate() throws TelegramApiValidationException {
        super.validate();
    }

    @Override
    public String getType() {
        return TYPE;
    }
}

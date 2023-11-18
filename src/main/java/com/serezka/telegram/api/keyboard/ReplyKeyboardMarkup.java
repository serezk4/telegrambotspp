package com.serezka.telegram.api.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;

import java.util.List;

/**
 * @author Ruben Bermudez
 * @version 1.0+ (finalized by @serezk4)
 * This object represents a custom keyboard with reply options.
 */

@JsonDeserialize
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyKeyboardMarkup implements ReplyKeyboard {
    @JsonProperty("keyboard")
    @Singular(value = "keyboardRow")
    @Setter @NonNull
    private List<KeyboardRow> keyboard;

    @JsonProperty("resize_keyboard")
    @Builder.Default
    private Boolean resizeKeyboard = false;

    @JsonProperty("one_time_keyboard")
    private Boolean oneTimeKeyboard;

    @JsonProperty("selective")
    private Boolean selective;

    @JsonProperty("input_field_placeholder")
    private String inputFieldPlaceholder;

    @JsonProperty("is_persistent")
    @Builder.Default
    private Boolean isPersistent = true;

    @Override
    public void validate() throws TelegramApiValidationException {
        if (inputFieldPlaceholder != null && (inputFieldPlaceholder.isEmpty() || inputFieldPlaceholder.length() > 64))
            throw new TelegramApiValidationException("InputFieldPlaceholder must be between 1 and 64 characters", this);
        for (KeyboardRow keyboardButtons : keyboard)
            keyboardButtons.validate();
    }
}

package com.serezka.telegram.api.meta.api.objects.webapp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import com.serezka.telegram.api.meta.api.interfaces.BotApiObject;
import com.serezka.telegram.api.meta.api.interfaces.Validable;
import com.serezka.telegram.api.meta.exceptions.TelegramApiValidationException;

/**
 * @author Ruben Bermudez
 * @version 6.0
 *
 */
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class WebAppInfo implements Validable, BotApiObject {
    private static final String URL_FIELD = "url";

    @JsonProperty(URL_FIELD)
    @NonNull
    private String url; ///< An HTTPS URL of a web app to be opened with additional data as specified at ...

    @Override
    public void validate() throws TelegramApiValidationException {
        if (url.isEmpty()) {
            throw new TelegramApiValidationException("Url can't be empty", this);
        }
    }
}

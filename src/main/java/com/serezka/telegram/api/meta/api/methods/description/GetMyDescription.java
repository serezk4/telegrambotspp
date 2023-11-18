package com.serezka.telegram.api.meta.api.methods.description;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.serezka.telegram.api.meta.api.methods.BotApiMethod;
import com.serezka.telegram.api.meta.api.objects.description.BotDescription;
import com.serezka.telegram.api.meta.exceptions.TelegramApiRequestException;

/**
 * @author Ruben Bermudez
 * @version 6.6
 * Use this method to get the current bot description for the given user language.
 * Returns BotDescription on success.
 */
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMyDescription extends BotApiMethod<BotDescription> {
    public static final String PATH = "getMyDescription";

    private static final String LANGUAGE_CODE_FIELD = "language_code";

    /**
     * Optional
     * A two-letter ISO 639-1 language code.
     * If empty, the description will be applied to all users for whose language there is no dedicated description.
     */
    @JsonProperty(LANGUAGE_CODE_FIELD)
    private String languageCode;

    @Override
    public BotDescription deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, BotDescription.class);
    }

    @Override
    public String getMethod() {
        return PATH;
    }
}

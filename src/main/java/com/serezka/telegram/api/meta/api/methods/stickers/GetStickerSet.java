package com.serezka.telegram.api.meta.api.methods.stickers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import com.serezka.telegram.api.meta.api.methods.BotApiMethod;
import com.serezka.telegram.api.meta.api.objects.stickers.StickerSet;
import com.serezka.telegram.api.meta.exceptions.TelegramApiRequestException;
import com.serezka.telegram.api.meta.exceptions.TelegramApiValidationException;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * Use this method to get a sticker set. On success, a StickerSet object is returned.
 */
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class GetStickerSet extends BotApiMethod<StickerSet> {
    private static final String PATH = "getStickerSet";

    private static final String NAME_FIELD = "name";

    @JsonProperty(NAME_FIELD)
    @NonNull
    private String name; ///< Name of the sticker set

    @Override
    public String getMethod() {
        return PATH;
    }

    @Override
    public StickerSet deserializeResponse(String answer) throws TelegramApiRequestException {
        return deserializeResponse(answer, StickerSet.class);
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (name.isEmpty()) {
            throw new TelegramApiValidationException("Name can't be null", this);
        }
    }
}

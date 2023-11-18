package com.serezka.telegram.api.meta.api.objects.replykeyboard;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.serezka.telegram.api.meta.api.interfaces.BotApiObject;
import com.serezka.telegram.api.meta.api.interfaces.Validable;
import com.serezka.telegram.api.meta.api.objects.replykeyboard.serialization.KeyboardDeserializer;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * Reply keyboard abstract type
 */
@JsonDeserialize(using = KeyboardDeserializer.class)
public interface ReplyKeyboard extends BotApiObject, Validable {
}

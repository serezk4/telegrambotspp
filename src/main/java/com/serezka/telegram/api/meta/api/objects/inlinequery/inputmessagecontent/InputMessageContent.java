package com.serezka.telegram.api.meta.api.objects.inlinequery.inputmessagecontent;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.serezka.telegram.api.meta.api.interfaces.BotApiObject;
import com.serezka.telegram.api.meta.api.interfaces.Validable;
import com.serezka.telegram.api.meta.api.objects.inlinequery.inputmessagecontent.serialization.InputMessageContentDeserializer;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * This object represents the content of a message to be sent as a result of an inline
 * query.
 */
@JsonDeserialize(using = InputMessageContentDeserializer.class)
public interface InputMessageContent extends Validable, BotApiObject {
}

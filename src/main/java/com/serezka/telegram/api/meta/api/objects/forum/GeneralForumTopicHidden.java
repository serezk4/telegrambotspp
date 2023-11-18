package com.serezka.telegram.api.meta.api.objects.forum;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.serezka.telegram.api.meta.api.interfaces.BotApiObject;

/**
 * This object represents a service message about General forum topic hidden in the chat.
 * Currently holds no information.
 * @author Ruben Bermudez
 * @version 6.4
 */
@SuppressWarnings("WeakerAccess")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@AllArgsConstructor
public class GeneralForumTopicHidden implements BotApiObject {
}

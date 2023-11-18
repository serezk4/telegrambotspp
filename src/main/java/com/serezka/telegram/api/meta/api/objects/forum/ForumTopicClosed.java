package com.serezka.telegram.api.meta.api.objects.forum;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.serezka.telegram.api.meta.api.interfaces.BotApiObject;

/**
 * This object represents a service message about a forum topic closed in the chat.
 * Currently holds no information.
 * @author Ruben Bermudez
 * @version 6.3
 */
@SuppressWarnings("WeakerAccess")
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ForumTopicClosed implements BotApiObject {

}

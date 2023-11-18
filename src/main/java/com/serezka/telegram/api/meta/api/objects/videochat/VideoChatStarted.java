package com.serezka.telegram.api.meta.api.objects.videochat;

import lombok.*;
import com.serezka.telegram.api.meta.api.interfaces.BotApiObject;

/**
 * @author Ruben Bermudez
 * @version 6.0
 *
 * This object represents a service message about a video chat started in the chat.
 * Currently holds no information.
 */
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class VideoChatStarted implements BotApiObject {
}

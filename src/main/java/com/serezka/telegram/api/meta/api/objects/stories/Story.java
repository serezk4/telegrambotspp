package com.serezka.telegram.api.meta.api.objects.stories;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.serezka.telegram.api.meta.api.interfaces.BotApiObject;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
public class Story implements BotApiObject {
}

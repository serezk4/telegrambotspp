package com.serezka.telegram.session.menu;

import com.serezka.telegram.util.keyboard.Keyboard;
import com.serezka.telegram.util.keyboard.type.Inline;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

public class MenuButton extends Inline.Button {
    public MenuButton(String text, String link, String... params) {
        super(text, Stream.concat(Stream.of(link), Arrays.stream(params)).toList());
    }

    public MenuButton(String text, String link) {
        super(text, Collections.singletonList(link));
    }
}

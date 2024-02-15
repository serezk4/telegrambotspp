package com.serezka.telegram.util.keyboard.type;

import com.serezka.telegram.util.keyboard.Keyboard;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.util.*;
import java.util.stream.IntStream;

public class Inline {
    public static InlineKeyboardMarkup getStaticKeyboard(Button[][] buttonsData) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        Arrays.stream(buttonsData).forEach(row ->
                rows.add(Arrays.stream(row).
                        filter(Objects::nonNull).
                        map(Inline::getButton).
                        toList()
                )
        );

        return new InlineKeyboardMarkup(rows);
    }

    public static InlineKeyboardMarkup getResizableKeyboard(List<? extends Button> buttonsData, int rowSize) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        Queue<Button> buttonsQueue = new PriorityQueue<>(buttonsData);

        while (!buttonsQueue.isEmpty())
            rows.add(
                    IntStream.range(0, Math.min(buttonsQueue.size(), rowSize))
                            .mapToObj(i -> buttonsQueue.poll())
                            .filter(Objects::nonNull)
                            .map(Inline::getButton)
                            .toList()
            );

        return new InlineKeyboardMarkup(rows);
    }

    private static InlineKeyboardButton getButton(String text, String callbackData, long id) {
        InlineKeyboardButton tempInlineButton = new InlineKeyboardButton(text);
        tempInlineButton.setCallbackData(id + Keyboard.Delimiter.SERVICE + callbackData);

        return tempInlineButton;
    }

    private static InlineKeyboardButton getButton(Button button) {
        return button.getWebAppInfo() != null ?
                getButton(button.getText(), button.getWebAppInfo(), button.getSessionId()) :
                getButton(button.getText(), String.join(Keyboard.Delimiter.SERVICE, button.getData()), button.getSessionId());
    }

    private static InlineKeyboardButton getButton(String text, WebAppInfo webAppInfo, long id) {
        InlineKeyboardButton tempInlineButton = new InlineKeyboardButton(text);
        tempInlineButton.setWebApp(webAppInfo);

        return tempInlineButton;
    }

    @Getter @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Builder @AllArgsConstructor
    public static class Button {
        private static long counter = 0;

        String text;
        List<String> data;
        long sessionId;
        WebAppInfo webAppInfo;

        public Button(String text, List<String> data) {
            this(text, data, counter++);
        }

        public Button(String text, WebAppInfo webAppInfo) {
            this(text, webAppInfo, counter++);
        }

        public Button(String text, List<String> data, long sessionId) {
            this.text = text;
            this.sessionId = sessionId;
            this.data = data;
            this.webAppInfo = null;
        }

        public Button(String text, WebAppInfo webAppInfo, long sessionId) {
            this.text = text;
            this.sessionId = sessionId;
            this.webAppInfo = webAppInfo;
            this.data = null;
        }
    }
}

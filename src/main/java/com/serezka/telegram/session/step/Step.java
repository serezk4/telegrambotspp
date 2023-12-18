package com.serezka.telegram.session.step;

import com.serezka.telegram.api.meta.api.objects.Update;
import com.serezka.telegram.api.meta.api.objects.replykeyboard.ReplyKeyboard;
import com.serezka.telegram.bot.Bot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter @RequiredArgsConstructor
public abstract class Step {
    public abstract Data get(Bot bot, Update update, StepSession session, String exception);

    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Getter
    public static class Data extends Step {
        String text;
        ReplyKeyboard replyKeyboard;

        public Data(String text, ReplyKeyboard replyKeyboard) {
            this.text = text;
            this.replyKeyboard = replyKeyboard;
        }

        public Data(String text) {
            this.text = text;
            this.replyKeyboard = null;
        }

        @Override
        public Data get(Bot bot, Update update, StepSession session, String exception) {
            return new Data(String.join("\n", text, exception), replyKeyboard);
        }
    }
}

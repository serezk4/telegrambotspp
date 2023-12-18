package com.serezka.telegram.session.step;

import com.serezka.telegram.api.meta.api.methods.send.SendMessage;
import com.serezka.telegram.api.meta.api.methods.updatingmessages.EditMessageText;
import com.serezka.telegram.api.meta.api.objects.Update;
import com.serezka.telegram.api.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.command.Command;
import com.serezka.telegram.session.Session;
import com.serezka.telegram.util.Keyboard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class StepSession extends Session {
    Command command;

    // session init
    List<Step> steps = new ArrayList<>();
    @NonFinal @Getter int currentPosition;

    // session data
    @NonFinal
    Map<Integer, String> data = new HashMap<>();

    protected void jumpTo(int position) {
        currentPosition = position;
    }

    protected void jumpTo(Step step) {
        currentPosition = steps.indexOf(step);
        if (currentPosition == -1) log.warn("can't jump to step " + step.getClass().getName());
    }

    @Override
    protected void init(Bot bot, Update update) {
        Step initStep = steps.get(currentPosition);
        Step.Data initData = initStep.get(bot, update, this, "");

        bot.execute(SendMessage.builder()
                .chatId(update).text(initData.getText())
                .replyMarkup(initData.getReplyKeyboard()).build());
    }

    @Override
    protected void getNext(Bot bot, Update update) {
        if (currentPosition == -1) {
            log.warn("can't jump to {}", currentPosition);
            destroy(bot, update);
            return;
        }

        data.put(currentPosition, update.getText());

        // go to next step
        ++currentPosition;

        if (steps.size() == currentPosition) {
            log.info("executing command {}", command.getClass().getName());
            command.execute(bot, update);
            return;
        }

        Step currentStep = steps.get(currentPosition);
        Step.Data stepData = currentStep.get(bot, update, this, "");

        if (update.hasCallbackQuery()) {
            bot.execute(EditMessageText.builder()
                    .chatId(update).messageId(update)
                    .text(stepData.getText()).replyMarkup((InlineKeyboardMarkup) stepData.getReplyKeyboard())
                    .build());
        } else {
            bot.execute(SendMessage.builder()
                    .chatId(update).text(stepData.getText())
                    .replyMarkup(stepData.getReplyKeyboard()).build());
        }

    }
}

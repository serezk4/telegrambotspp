package com.serezka.telegram.session.step;

import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.session.Session;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Log4j2
public class StepSession extends Session {

    // session init
    List<Step> steps = new ArrayList<>();
    @NonFinal @Getter int currentPosition;

    // session data
    @NonFinal
    Map<Integer, String> data = new HashMap<>();

    public StepSession(Bot bot, long chatId) {
        super(bot, chatId);
    }

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

        bot.executeAsync(SendMessage.builder()
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
//            log.info("executing command {}", command.getClass().getName());
//            command.execute(bot, update);
//            return;
        }

        Step currentStep = steps.get(currentPosition);
        Step.Data stepData = currentStep.get(bot, update, this, "");

        if (update.hasCallbackQuery()) {
            bot.executeAsync(EditMessageText.builder()
                    .chatId(update).messageId(update)
                    .text(stepData.getText()).replyMarkup((InlineKeyboardMarkup) stepData.getReplyKeyboard())
                    .build());
        } else {
            bot.executeAsync(SendMessage.builder()
                    .chatId(update).text(stepData.getText())
                    .replyMarkup(stepData.getReplyKeyboard()).build());
        }

    }
}

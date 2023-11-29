package com.serezka.telegram.api.meta.api.methods.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.serezka.telegram.api.meta.api.objects.Update;
import com.serezka.telegram.util.Keyboard;
import lombok.*;
import lombok.experimental.Tolerate;
import com.serezka.telegram.api.meta.api.methods.ParseMode;
import com.serezka.telegram.api.meta.api.methods.botapimethods.BotApiMethodMessage;
import com.serezka.telegram.api.meta.api.objects.MessageEntity;
import com.serezka.telegram.api.meta.api.objects.replykeyboard.ReplyKeyboard;
import com.serezka.telegram.api.meta.exceptions.TelegramApiValidationException;

import java.util.List;

/**
 * @author Ruben Bermudez
 * @version 1.0+ (finalized by @serezk4)
 * Use this method to send text messages. On success, the sent Message is returned.
 */

@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@Data
public class SendMessage extends BotApiMethodMessage {
    @Builder.Default
    public String method = "sendmessage";

    @JsonProperty("chat_id")
    @NonNull
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @JsonProperty("text")
    @NonNull
    private String text;

    @JsonProperty("parse_mode")
    @Builder.Default
    private String parseMode = ParseMode.MARKDOWN;

    @JsonProperty("disable_web_page_preview")
    private Boolean disableWebPagePreview;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    @JsonProperty("reply_markup")
    @JsonDeserialize
    @Builder.Default
    private ReplyKeyboard replyMarkup = Keyboard.Reply.DEFAULT;

    @JsonProperty("entities")
    private List<MessageEntity> entities;

    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;

    @JsonProperty("protect_content")
    private Boolean protectContent;

    @Tolerate
    public void setChatId(@NonNull Long chatId) {
        this.chatId = chatId.toString();
    }

    @Override
    public void validate() throws TelegramApiValidationException {
        if (chatId.isEmpty())
            throw new TelegramApiValidationException("ChatId parameter can't be empty", this);
        if (text.isEmpty())
            throw new TelegramApiValidationException("Text parameter can't be empty", this);
        if (parseMode != null && (entities != null && !entities.isEmpty()))
            throw new TelegramApiValidationException("Parse mode can't be enabled if Entities are provided", this);
        if (replyMarkup != null) replyMarkup.validate();
    }

    public static class SendMessageBuilder {
        @Tolerate
        public SendMessageBuilder text(Object object) {
            this.text = object.toString();
            return this;
        }

        @Tolerate
        public SendMessageBuilder replyToMessageId(Update update) {
            this.replyToMessageId = update.getMessageId();
            return this;
        }

        @Tolerate
        public SendMessageBuilder text(long num) {
            this.text = String.valueOf(num);
            return this;
        }

        @Tolerate
        public SendMessageBuilder text(int num) {
            this.text = String.valueOf(num);
            return this;
        }

        @Tolerate
        public SendMessageBuilder text(@NonNull Number num) {
            this.text = num.toString();
            return this;
        }

        @Tolerate
        public SendMessageBuilder chatId(@NonNull Update update) {
            this.chatId = String.valueOf(update.getChatId());
            return this;
        }

        @Tolerate
        public SendMessageBuilder chatId(@NonNull Long chatId) {
            this.chatId = chatId.toString();
            return this;
        }
    }
}
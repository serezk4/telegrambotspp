package com.serezka.telegram.api.meta.api.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.serezka.database.model.User;
import com.serezka.telegram.api.meta.api.interfaces.BotApiObject;
import com.serezka.telegram.api.meta.api.objects.inlinequery.ChosenInlineQuery;
import com.serezka.telegram.api.meta.api.objects.inlinequery.InlineQuery;
import com.serezka.telegram.api.meta.api.objects.payments.PreCheckoutQuery;
import com.serezka.telegram.api.meta.api.objects.payments.ShippingQuery;
import com.serezka.telegram.api.meta.api.objects.polls.Poll;
import com.serezka.telegram.api.meta.api.objects.polls.PollAnswer;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * This object represents an incoming update.
 *
 * @author Ruben Bermudez
 * @version 1.1 (finalized by @serezk4)
 * @apiNote Only one of the optional parameters can be present in any given update.
 */
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Update implements BotApiObject {
    @JsonProperty("update_id")
    Integer updateId;

    @JsonProperty("message")
    Message message;

    @JsonProperty("inline_query")
    InlineQuery inlineQuery;

    @JsonProperty("chosen_inline_result")
    ChosenInlineQuery chosenInlineQuery;

    @JsonProperty("callback_query")
    CallbackQuery callbackQuery;

    @JsonProperty("edited_message")
    Message editedMessage;

    @JsonProperty("channel_post")
    Message channelPost;

    @JsonProperty("edited_channel_post")
    Message editedChannelPost;

    @JsonProperty("shipping_query")
    ShippingQuery shippingQuery;

    @JsonProperty("pre_checkout_query")
    PreCheckoutQuery preCheckoutQuery;

    @JsonProperty("poll")
    Poll poll;

    @JsonProperty("poll_answer")
    PollAnswer pollAnswer;

    @JsonProperty("my_chat_member")
    ChatMemberUpdated myChatMember;

    @JsonProperty("chat_member")
    ChatMemberUpdated chatMember;

    @JsonProperty("chat_join_request")
    ChatJoinRequest chatJoinRequest;

    public boolean hasMessage() {
        return message != null;
    }

    public boolean hasInlineQuery() {
        return inlineQuery != null;
    }

    public boolean hasChosenInlineQuery() {
        return chosenInlineQuery != null;
    }

    public boolean hasCallbackQuery() {
        return callbackQuery != null;
    }

    public boolean hasEditedMessage() {
        return editedMessage != null;
    }

    public boolean hasChannelPost() {
        return channelPost != null;
    }

    public boolean hasEditedChannelPost() {
        return editedChannelPost != null;
    }

    public boolean hasShippingQuery() {
        return shippingQuery != null;
    }

    public boolean hasPreCheckoutQuery() {
        return preCheckoutQuery != null;
    }

    public boolean hasPoll() {
        return poll != null;
    }

    public boolean hasPollAnswer() {
        return pollAnswer != null;
    }

    public boolean hasMyChatMember() {
        return myChatMember != null;
    }

    public boolean hasChatMember() {
        return chatMember != null;
    }

    public boolean hasChatJoinRequest() {
        return chatJoinRequest != null;
    }

    // user stuff
    @JsonIgnore
    User databaseUser = null;

    public Update setDatabaseUser(User databaseUser) {
        this.databaseUser = databaseUser;
        return this;
    }

    public User getDatabaseUser() {
        if (databaseUser == null) throw new NullPointerException("user can't be null!");
        return databaseUser;
    }

    // cache stuff
    @JsonIgnore
    QueryType queryType = null;
    @JsonIgnore
    List<Flag> messageFlags = null;
    @JsonIgnore
    Integer cacheMessageId = null;
    @JsonIgnore
    Long cachedChatId = null;
    @JsonIgnore
    String cachedUsername = null;
    @JsonIgnore
    String cachedText = null;


    public enum QueryType {
        MESSAGE, INLINE_QUERY, CHOSEN_INLINE_QUERY, CALLBACK_QUERY,
        EDITED_MESSAGE, CHANNEL_POST, EDITED_CHANNEL_POST, SHIPPING_QUERY,
        PRE_CHECKOUT_QUERY, POLL, POLL_ANSWER, CHAT_JOIN_REQUEST,
        CHAT_MEMBER_UPDATED_MY, CHAT_MEMBER_UPDATED, UNKNOWN;
    }

    public enum Flag { // if needed add some fields ~
        TEXT, ENTITIES, CAPTION_ENTITIES, AUDIO, DOCUMENT, PHOTO, STICKER,
        VIDEO, CONTACT, LOCATION, VENUE, ANIMATION, PINNED_MESSAGE, NEW_CHAT_MEMBERS,
        LEFT_CHAT_MEMBER, NEW_CHAT_TITLE, NEW_CHAT_PHOTO, DELETE_CHAT_PHOTO,
        GROUPCHAT_CREATED, REPLY_TO_MESSAGE, VOICE, CAPTION, SUPERGROUP_CREATED,
        CHANNEL_CHAT_CREATED, MIGRATE_TO_CHAT_ID, MIGRATE_FROM_CHAT_ID, EDIT_DATE, GAME,
        FORWARD_FROM_MESSAGE_ID, INVOICE, SUCCESSFUL_PAYMENT, VIDEO_NOTE, AUTHOR_SIGNATURE,
        FORWARD_SIGNATURE, MEDIA_GROUP_ID, CONNECTED_WEBSITE, PASSPORT_DATA, FORWARD_SENDER_NAME,
        POLL, REPLY_MARKUP, DICE, VIA_BOT, SENDER_CHAT, PROXIMITY_ALERT_TRIGGERED, MESSAGE_AUTO_DELETE_TIMER_CHANGED,
        IS_AUTOMATIC_FORWARD, HAS_PROTECTED_CONTENT, WEB_APP_DATA, VIDEO_CHAT_STARTED, VIDEO_CHAT_ENDED,
        VIDE_CHAT_PARTICIPANTS_INVITED, VIDEO_CHAT_SCHEDULED, IS_TOPIC_MESSAGE, FORUM_TOPIC_CREATED, FORUM_TOPIC_CLOSED,
        FORUM_TOPIC_REOPENED, FORUM_TOPIC_EDITED, GENERAL_FORUM_TOPIC_HIDDEN, GENERAL_FORUM_TOPIC_UNHIDDEN,
        WRITE_ACCESS_ALLOWED, HAS_MEDIA_SPOILER, USER_SHARED, CHAT_SHARED, UNKNOWN;
    }

    // ~10.000 nano sec to process
    @SneakyThrows
    public List<Flag> flags() {
        // cache
        if (queryType == null) queryType = queryType(this);

        if (messageFlags != null) return messageFlags;

        if (queryType != QueryType.MESSAGE) return Collections.emptyList();
        final Message message = getMessage();

        List<Flag> flags = new ArrayList<>();
        if (message.hasText()) flags.add(Flag.TEXT);
        if (message.hasEntities()) flags.add(Flag.ENTITIES);
        if (message.getCaptionEntities() != null) flags.add(Flag.CAPTION_ENTITIES); // TODO
        if (message.hasAudio()) flags.add(Flag.AUDIO);
        if (message.hasDocument()) flags.add(Flag.DOCUMENT);
        if (message.hasPhoto()) flags.add(Flag.PHOTO);
        if (message.hasSticker()) flags.add(Flag.STICKER);
        if (message.hasVideo()) flags.add(Flag.VIDEO);
        if (message.hasContact()) flags.add(Flag.CONTACT);
        if (message.hasLocation()) flags.add(Flag.LOCATION);
        if (message.getVenue() != null) flags.add(Flag.VENUE); // TODO
        if (message.hasAnimation()) flags.add(Flag.ANIMATION);
        if (message.getPinnedMessage() != null) flags.add(Flag.PINNED_MESSAGE); // TODO
        if (message.getNewChatMembers() != null) flags.add(Flag.NEW_CHAT_MEMBERS);
        if (message.getLeftChatMember() != null) flags.add(Flag.LEFT_CHAT_MEMBER);
        if (message.getNewChatTitle() != null) flags.add(Flag.NEW_CHAT_TITLE);
        if (message.getNewChatPhoto() != null) flags.add(Flag.NEW_CHAT_PHOTO);
        if (message.getDeleteChatPhoto() != null) flags.add(Flag.DELETE_CHAT_PHOTO);
        if (message.getGroupchatCreated() != null) flags.add(Flag.GROUPCHAT_CREATED);
        if (message.getReplyToMessage() != null) flags.add(Flag.REPLY_TO_MESSAGE);
        if (message.hasVoice()) flags.add(Flag.VOICE);
        if (message.getCaption() != null) flags.add(Flag.CAPTION);
        if (message.getSuperGroupCreated() != null) flags.add(Flag.SUPERGROUP_CREATED);
        if (message.getChannelChatCreated() != null) flags.add(Flag.CHANNEL_CHAT_CREATED);
        if (message.getMigrateToChatId() != null) flags.add(Flag.MIGRATE_TO_CHAT_ID);
        if (message.getMigrateFromChatId() != null) flags.add(Flag.MIGRATE_FROM_CHAT_ID);
        if (message.getEditDate() != null) flags.add(Flag.EDIT_DATE);
        if (message.getGame() != null) flags.add(Flag.GAME);
        if (message.getForwardFromMessageId() != null) flags.add(Flag.FORWARD_FROM_MESSAGE_ID);
        if (message.hasInvoice()) flags.add(Flag.INVOICE);
        if (message.hasSuccessfulPayment()) flags.add(Flag.SUCCESSFUL_PAYMENT);
        if (message.hasVideoNote()) flags.add(Flag.VIDEO_NOTE);
        if (message.getAuthorSignature() != null) flags.add(Flag.AUTHOR_SIGNATURE);
        if (message.getForwardSignature() != null) flags.add(Flag.FORWARD_SIGNATURE);
        if (message.getMediaGroupId() != null) flags.add(Flag.MEDIA_GROUP_ID);
        if (message.getConnectedWebsite() != null) flags.add(Flag.CONNECTED_WEBSITE);
        if (message.hasPassportData()) flags.add(Flag.PASSPORT_DATA);
        if (message.getForwardSenderName() != null) flags.add(Flag.FORWARD_SENDER_NAME);
        if (message.hasPoll()) flags.add(Flag.POLL);
        if (message.hasReplyMarkup()) flags.add(Flag.REPLY_MARKUP);
        if (message.hasDice()) flags.add(Flag.DICE);
        if (message.hasViaBot()) flags.add(Flag.VIA_BOT);
        if (message.getSenderChat() != null) flags.add(Flag.SENDER_CHAT);
        if (message.getProximityAlertTriggered() != null) flags.add(Flag.PROXIMITY_ALERT_TRIGGERED);
        if (message.getMessageAutoDeleteTimerChanged() != null) flags.add(Flag.MESSAGE_AUTO_DELETE_TIMER_CHANGED);
        if (message.getIsAutomaticForward() != null && message.getIsAutomaticForward())
            flags.add(Flag.IS_AUTOMATIC_FORWARD);
        if (message.getHasProtectedContent() != null && message.getHasProtectedContent())
            flags.add(Flag.HAS_PROTECTED_CONTENT);
        if (message.getWebAppData() != null) flags.add(Flag.WEB_APP_DATA);
        if (message.getVideoChatStarted() != null) flags.add(Flag.VIDEO_CHAT_STARTED);
        if (message.getVideoChatEnded() != null) flags.add(Flag.VIDEO_CHAT_ENDED);
        if (message.getVideoChatParticipantsInvited() != null) flags.add(Flag.VIDE_CHAT_PARTICIPANTS_INVITED);
        if (message.getVideoChatScheduled() != null) flags.add(Flag.VIDEO_CHAT_SCHEDULED);
        if (message.getIsTopicMessage() != null && message.getIsTopicMessage()) flags.add(Flag.IS_TOPIC_MESSAGE);
        if (message.getForumTopicCreated() != null) flags.add(Flag.FORUM_TOPIC_CREATED);
        if (message.getForumTopicEdited() != null) flags.add(Flag.FORUM_TOPIC_EDITED);
        if (message.getForumTopicReopened() != null) flags.add(Flag.FORUM_TOPIC_REOPENED);
        if (message.getForumTopicClosed() != null) flags.add(Flag.FORUM_TOPIC_CLOSED);
        if (message.getGeneralForumTopicHidden() != null) flags.add(Flag.GENERAL_FORUM_TOPIC_HIDDEN);
        if (message.getGeneralForumTopicUnhidden() != null) flags.add(Flag.GENERAL_FORUM_TOPIC_UNHIDDEN);
        if (message.getWriteAccessAllowed() != null) flags.add(Flag.WRITE_ACCESS_ALLOWED);
        if (message.getHasMediaSpoiler() != null && message.getHasMediaSpoiler()) flags.add(Flag.HAS_MEDIA_SPOILER);
        if (message.getUserShared() != null) flags.add(Flag.USER_SHARED);
        if (message.getChatShared() != null) flags.add(Flag.CHAT_SHARED);

        messageFlags = flags;

        if (messageFlags.isEmpty()) messageFlags.add(Flag.UNKNOWN);
        return messageFlags;
    }

    private static QueryType queryType(Update update) {
        if (update.hasMessage()) return QueryType.MESSAGE;
        if (update.hasCallbackQuery()) return QueryType.CALLBACK_QUERY;
        if (update.hasInlineQuery()) return QueryType.INLINE_QUERY;
        if (update.hasChosenInlineQuery()) return QueryType.CHOSEN_INLINE_QUERY;
        if (update.hasEditedMessage()) return QueryType.EDITED_MESSAGE;
        if (update.hasChannelPost()) return QueryType.CHANNEL_POST;
        if (update.hasEditedChannelPost()) return QueryType.EDITED_CHANNEL_POST;
        if (update.hasShippingQuery()) return QueryType.SHIPPING_QUERY;
        if (update.hasPreCheckoutQuery()) return QueryType.PRE_CHECKOUT_QUERY;
        if (update.hasPoll()) return QueryType.POLL;
        if (update.hasPollAnswer()) return QueryType.POLL_ANSWER;
        if (update.hasChatJoinRequest()) return QueryType.CHAT_JOIN_REQUEST;
        if (update.hasMyChatMember()) return QueryType.CHAT_MEMBER_UPDATED_MY;
        if (update.hasChatMember()) return QueryType.CHAT_MEMBER_UPDATED;

        return QueryType.UNKNOWN;
    }

    public int getMessageId() {
        if (queryType == null) queryType = queryType(this);
        if (cacheMessageId != null) return cacheMessageId;

        return cacheMessageId = switch (queryType) {
            case MESSAGE -> getMessage().getMessageId();
            case CALLBACK_QUERY -> getCallbackQuery().getMessage().getMessageId();
            case CHOSEN_INLINE_QUERY -> Integer.parseInt(getChosenInlineQuery().getInlineMessageId());
            case EDITED_MESSAGE -> getEditedMessage().getMessageId();
            case CHANNEL_POST -> getChannelPost().getMessageId();
            case EDITED_CHANNEL_POST -> getEditedChannelPost().getMessageId();
            default -> -1;
        };
    }

    public long getChatId() {
        if (queryType == null) queryType = queryType(this);
        if (cachedChatId != null) return cachedChatId;

        return cachedChatId = switch (queryType) {
            case MESSAGE -> getMessage().getChatId();
            case CALLBACK_QUERY -> getCallbackQuery().getMessage().getChatId();
            case EDITED_MESSAGE -> getEditedMessage().getChatId();
            case CHANNEL_POST -> getChannelPost().getChatId();
            case EDITED_CHANNEL_POST -> getEditedMessage().getChatId();
            case CHAT_JOIN_REQUEST -> getChatJoinRequest().getUserChatId();
            case CHAT_MEMBER_UPDATED_MY -> getMyChatMember().getChat().getId();
            case CHAT_MEMBER_UPDATED -> getChatMember().getChat().getId();
            default -> -1L;
        };
    }

    public boolean isUserMessage() {
        if (queryType == null) queryType = queryType(this);

        return switch (queryType) {
            case MESSAGE -> getMessage().isUserMessage();
            case INLINE_QUERY -> !getInlineQuery().getFrom().getIsBot();
            // TODO!!!!!! !! ! ! ! !
            default -> false;
        };
    }

    public String getUsername() {
        if (queryType == null) queryType = queryType(this);
        if (cachedUsername != null) return cachedUsername;

        return cachedUsername = switch (queryType) {
            case MESSAGE -> getMessage().getChat().getUserName();
            case CALLBACK_QUERY -> getCallbackQuery().getFrom().getUserName();
            case INLINE_QUERY -> getInlineQuery().getFrom().getUserName();
            case CHOSEN_INLINE_QUERY -> getChosenInlineQuery().getFrom().getUserName();
            case EDITED_MESSAGE -> getEditedMessage().getFrom().getUserName();
            case CHANNEL_POST -> getChannelPost().getFrom().getUserName();
            case EDITED_CHANNEL_POST -> getEditedChannelPost().getFrom().getUserName();
            case SHIPPING_QUERY -> getShippingQuery().getFrom().getUserName();
            case PRE_CHECKOUT_QUERY -> getPreCheckoutQuery().getFrom().getUserName();
            case POLL_ANSWER -> getPollAnswer().getUser().getUserName();
            case CHAT_JOIN_REQUEST -> getChatJoinRequest().getUser().getUserName();
            case CHAT_MEMBER_UPDATED_MY -> getMyChatMember().getFrom().getUserName();
            case CHAT_MEMBER_UPDATED -> getChatMember().getFrom().getUserName();
            default -> null;
        };
    }

    public String getText() {
        if (queryType == null) queryType = queryType(this);
        if (cachedText != null) return cachedText;

        // stuff for webapp
        if (getMessage() != null && getMessage().getWebAppData() != null)
            return cachedText = getMessage().getWebAppData().getData();
        if (hasMessage() && getMessage().hasDocument())
            return cachedText = Optional.ofNullable(getMessage().getCaption()).orElse("");

        return cachedText = switch (queryType) {
            case MESSAGE -> getMessage().hasText() ? getMessage().getText() : null;
            case CALLBACK_QUERY -> getCallbackQuery().getData();
            case INLINE_QUERY -> getInlineQuery().getQuery();
            case CHOSEN_INLINE_QUERY -> getChosenInlineQuery().getQuery();
            case EDITED_MESSAGE -> getEditedMessage().getText();
            case CHANNEL_POST -> getChannelPost().getText();
            case EDITED_CHANNEL_POST -> getEditedChannelPost().getText();
            default -> null;
        };
    }
}

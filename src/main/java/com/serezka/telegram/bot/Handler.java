package com.serezka.telegram.bot;

import com.serezka.database.model.User;
import com.serezka.database.service.UserService;
import com.serezka.telegram.api.SendMessage;
import com.serezka.telegram.util.AntiSpam;
import com.serezka.telegram.util.Read;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;

@Log4j2
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PropertySource("classpath:telegram.properties")
public class Handler {
    // handler per-init settings
//    @Getter
//    List<Command<? extends Session>> commands = new ArrayList<>();

    // database services
    UserService userService;
//    InviteService inviteService;

    // anti-spam services
    AntiSpam antiSpam;

    // cache
    Set<Long> authorized = Collections.newSetFromMap(new WeakHashMap<>());

//    public void addCommand(Command<? extends Session> command) {
//        commands.add(command);
//    }

    public void process(Bot bot, Qpdate update) {
        // validate query

        // check user

        // check session

        // get command

        // execute
    }

    private User getUser(Bot bot, long chatId, String username) {
        Optional<User> optionalUser = userService.findByChatId(chatId);

        if (optionalUser.isEmpty()) {
            log.warn("User exception (can't find or create) | {} : {}", username, chatId);
            bot.execute(SendMessage.builder()
                    .chatId(chatId).text("*Проблемы с сервисами БД*\nНапишите *@serezkk* для устранения проблемы.")
                    .build());
            return null;
        }

        authorized.add(chatId);
        return optionalUser.get();
    }

//    private boolean checkAuth(TBot bot, TUpdate update) {
//        final long chatId = update.getChatId();
//        final String username = update.getUsername();
//        final String text = update.getText();
//
//        // check auth
//        if (!authorized.contains(chatId) && !userService.existsByUsernameOrChatId(username, chatId) && !inviteService.existsByCode(text)) {
//            bot.execute(SendMessage.builder()
//                    .chatId(chatId).text("*Вы еще не авторизовались в боте.*\n_Введите токен, который вы получили:_")
//                    .parseMode(ParseMode.MARKDOWN)
//                    .build());
//
//            return true;
//        }
//
//        if (!authorized.contains(chatId) && !userService.existsByUsernameOrChatId(username, chatId)) {
//            // if user entered token we will message him about it and add new row in database
//
//            bot.execute(DeleteMessage.builder()
//                    .chatId(chatId).messageId(update.getMessageId())
//                    .build());
//
//            bot.execute(SendMessage.builder()
//                    .chatId(chatId).text("✅ *Вы успешно авторизовались!*")
//                    .parseMode(ParseMode.MARKDOWN)
//                    .build());
//
//            bot.execute(SendMessage.builder()
//                    .chatId(chatId).text("""
//                            админ данного паблика *@serezkk*.
//
//                            ℹ️ Работает на модели *gpt-4*.
//                            ℹ️ *Поддерживает* _(практически)_ *все файлы для запросов*.
//                                   (.txt, .docx, .xml, .py, .cpp, ...)
//                            ℹ️ *Работает* `24/7` _(иногда выключается для обновления)_.
//
//                            *Нажимайте на кнопку* `\uD83D\uDDD1️ Очистить историю` *почаще*.
//                            """)
//                    .parseMode(ParseMode.MARKDOWN).build());
//
//            // save user to database
//            userService.save(new User(chatId, username, 0L /*TODO*/));
//
//            return true;
//        }
//        return false;
//    }

    @Deprecated // for this bot
    public String getHelp(int adminLvl) {
//        StringBuilder help = new StringBuilder("Кажется, вы ошиблись в команде. Список допустимых команд:\n");
//        help.append(commands.stream()
//                .filter(command -> command.getAdminLvl() <= adminLvl)
//                .map(command -> String.format(" - <b>%s</b> - %s%n", command.getNames(), command.getHelp()))
//                .collect(Collectors.joining()));
//        return help.toString();

        return "todo";
    }
}

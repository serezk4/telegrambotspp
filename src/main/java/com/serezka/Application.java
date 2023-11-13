package com.serezka;


import com.serezka.telegram.api.SendMessage;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.bot.Handler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Application implements ApplicationRunner {
    @Getter
    private static final LocalDateTime startTime = LocalDateTime.now();

    // bot stuff
    Handler handler;
    Bot bot;

    // ..

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        bot.setHandler(handler);
//
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//        telegramBotsApi.registerBot(bot);
    }

}

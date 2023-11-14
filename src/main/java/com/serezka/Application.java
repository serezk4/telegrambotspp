package com.serezka;


import com.serezka.telegram.api.SendMessage;
import com.serezka.telegram.bot.Bot;
import com.serezka.telegram.bot.Handler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.time.LocalDateTime;
import java.util.Locale;

@SpringBootApplication
@RequiredArgsConstructor @Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Application implements ApplicationRunner {
    @Getter
    private static final LocalDateTime startTime = LocalDateTime.now();

    // bot stuff
    Handler handler;
    Bot bot;

    // ..

    public static void main(String[] args) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("lang/messages");
        messageSource.setDefaultLocale(Locale.of("us"));
        messageSource.setDefaultEncoding("UTF-8");

        System.out.println(messageSource.getMessage("hello", null, Locale.of("fr")));

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

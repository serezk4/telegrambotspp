package com.serezka.localization;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Localization {
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @RequiredArgsConstructor @Getter
    public enum Type {
        RU("русский", Locale.of("ru")),
        US("english", Locale.of("us"));

        String name;
        Locale locale;

        public static final Type DEFAULT = Type.RU;
    }

    ResourceBundleMessageSource messageSource;

    private Localization() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("lang/messages");
        messageSource.setDefaultLocale(Type.DEFAULT.getLocale());
        messageSource.setDefaultEncoding("UTF-8");

        this.messageSource = messageSource;
    }

    private static Localization instance = null;

    public static Localization getInstance() {
        if (instance == null) instance = new Localization();
        return instance;
    }

    public String get(String code, Type localization) {
        return messageSource.getMessage(code, null, localization.getLocale());
    }

    public String get(String code) {
        return messageSource.getMessage(code, null, Type.DEFAULT.getLocale());
    }
}

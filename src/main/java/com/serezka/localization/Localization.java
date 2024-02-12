package com.serezka.localization;

import com.serezka.database.model.DUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * Class for localization
 * Allows to get localized messages
 * @version 1.0
 */
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

    /**
     * Get localized message
     * @param code - message code
     * @param DUser - user to get localization
     * @return localized message
     */
    public String get(String code, DUser DUser) {
        return get(code, DUser.getLocalization());
    }

    /**
     * Get localized message
     * @param code - message code
     * @param localization - localization type
     * @return localized message
     */
    public String get(String code, Type localization) {
        return messageSource.getMessage(code, null, localization.getLocale());
    }

    /**
     * Get localized message
     * @param code - message code
     * @return localized message with default locale
     */
    public String get(String code) {
        return messageSource.getMessage(code, null, Type.DEFAULT.getLocale());
    }
}

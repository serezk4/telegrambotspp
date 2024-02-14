package com.serezka.database.model;

import com.serezka.localization.Localization;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Entity for user
 * Related to telegram
 *
 * @version 1.0
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class DUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // basic user data
    @Column(name = "chat_id", unique = true, nullable = false)
    @NonNull
    Long chatId;

    @NonNull
    @Column(nullable = false)
    String username;

    // bot settings for user
    @Builder.Default
    @NonNull
    Role role = Role.USER;

    @Builder.Default
    @NonNull
    Localization.Type localization = Localization.Type.DEFAULT;

    @Builder.Default
    @Column(name = "delete_command_summon_messages")
    @NonNull
    boolean deleteCommandSummonMessages = true;

    public DUser(@NonNull Long chatId, @NonNull String username) {
        this.chatId = chatId;
        this.username = username;
    }

    public DUser(@NonNull Long chatId, @NonNull String username, @NonNull Role role) {
        this.chatId = chatId;
        this.username = username;
        this.role = role;
    }

    public DUser(@NonNull Long chatId, @NonNull String username, @NonNull Role role, @NonNull Localization.Type localization) {
        this.chatId = chatId;
        this.username = username;
        this.role = role;
        this.localization = localization;
    }

    @AllArgsConstructor
    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public enum Role {
        USER("user", 0), ADMIN_1("admin #1", 100);

        String name;
        int adminLvl;

        public static final Role MAX = Role.ADMIN_1;
        public static final Role MIN = Role.USER;
    }
}

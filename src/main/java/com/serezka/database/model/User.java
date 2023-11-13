package com.serezka.database.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder @Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // basic user data
    @Column(name = "chat_id", unique = true)
    @NonNull
    Long chatId;

    @NonNull
    String username;

    // bot settings for user
    @Builder.Default
    @NonNull
    Role role = Role.USER;

    public User(@NonNull Long chatId, @NonNull String username) {
        this.chatId = chatId;
        this.username = username;
    }

    public User(@NonNull Long chatId, @NonNull String username, @NonNull Role role) {
        this.chatId = chatId;
        this.username = username;
        this.role = role;
    }

    @AllArgsConstructor
    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public enum Role {
        USER("user",0), ADMIN_1("admin #1",100);

        String name;
        int adminLvl;
    }
}

package com.serezka.database.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;

@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder @Data
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long chatId;
    Long messageId;
    Update.QueryType queryType;
    String contex;

    public History(Long chatId, Long messageId, Update.QueryType queryType, String contex) {
        this.chatId = chatId;
        this.messageId = messageId;
        this.queryType = queryType;
        this.contex = contex;
    }
}

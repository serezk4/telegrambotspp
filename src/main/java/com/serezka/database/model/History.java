package com.serezka.database.model;

import com.serezka.telegram.api.meta.api.objects.Update;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

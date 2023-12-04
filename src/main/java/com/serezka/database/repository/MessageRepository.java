package com.serezka.database.repository;

import com.serezka.database.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findTopByChatId(Long chatId);
    List<Message> findAllByChatId(Long chatId);

    void removeByMessageId(Integer messageId);
}

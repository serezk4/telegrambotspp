package com.serezka.database.repository;

import com.serezka.database.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<History, Long> {
    History findTopByChatId(Long chatId);
    List<History> findAllByChatId(Long chatId);

    void removeByMessageId(Integer messageId);
}

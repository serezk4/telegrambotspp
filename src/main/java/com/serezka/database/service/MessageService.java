package com.serezka.database.service;

import com.serezka.database.model.Message;
import com.serezka.database.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessageService {
    MessageRepository messageRepository;

    @Transactional
    public Message findTopByChatId(Long chatId) {
        return messageRepository.findTopByChatId(chatId);
    }

    @Transactional
    public List<Message> findAllByChatId(Long chatId) {
        return messageRepository.findAllByChatId(chatId);
    }

    @Transactional
    public void removeByMessageId(Integer messageId) {
        messageRepository.removeByMessageId(messageId);
    }
}

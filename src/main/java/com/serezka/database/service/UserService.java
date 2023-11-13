package com.serezka.database.service;

import com.serezka.database.model.User;
import com.serezka.database.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;

    // save
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    // count
    @Transactional
    public long count() {
        return userRepository.count();
    }

    @Transactional
    public long countByRole(User.Role role) {
        return userRepository.countAllByRole(role);
    }

    // find
    @Transactional
    public User findByChatIdOrUsername(Long chatId, String username) {
        return userRepository.findByChatIdOrUsername(chatId, username);
    }

    @Transactional
    public User findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public List<User> findAllByRole(User.Role role) {
        return userRepository.findAllByRole(role);
    }

    // exists
    @Transactional
    public boolean existsByChatIdOrUsername(Long chatId, String username) {
        return userRepository.existsByChatIdOrUsername(chatId, username);
    }

    @Transactional
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public boolean existsByChatId(Long chatId) {
        return userRepository.existsByChatId(chatId);
    }

    // remove
    public boolean removeByChatId(Long chatId) {
        return userRepository.removeByChatId(chatId);
    }

    public boolean removeById(Long id) {
        return userRepository.removeById(id);
    }
}

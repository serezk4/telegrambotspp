package com.serezka.database.service;

import com.serezka.database.model.User;
import com.serezka.database.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor @Log4j2
public class UserService {
    UserRepository userRepository;

    // save
    @Transactional
    public User save(User user) {
        log.debug("saved user {}", user.toString());
        return userRepository.save(user);
    }

    // count
    @Transactional
    public long count() {
        log.debug("counted all users");
        return userRepository.count();
    }

    @Transactional
    public long countByRole(User.Role role) {
        log.debug("counted users by role {}", role.getName());
        return userRepository.countAllByRole(role);
    }

    // find
    @Transactional
    public Optional<User> findByChatIdOrUsername(Long chatId, String username) {
        log.debug("trying to found user with chatId {} or username {}", chatId, username);
        return userRepository.findByChatIdOrUsername(chatId, username);
    }

    @Transactional
    public Optional<User> findByChatId(Long chatId) {
        log.debug("trying to find user with chatId {}", chatId);
        return userRepository.findByChatId(chatId);
    }

//    @Transactional TODO
//    public Optional<User> findByChatId(Update update) {
//        log.debug("trying to find user by update with chatId {}", update.getChatId());
//        return userRepository.findByChatId(update.getChatId());
//    }

    @Transactional
    public Optional<User> findByUsername(String username) {
        log.debug("trying to found user with username {}", username);
        return userRepository.findByUsername(username);
    }

    @Transactional
    public List<User> findAllByRole(User.Role role) {
        log.debug("trying to found all users with role {}", role.getName());
        return userRepository.findAllByRole(role);
    }

    // exists
    @Transactional
    public boolean existsByChatIdOrUsername(Long chatId, String username) {
        log.debug("checking if user with chatId {} or username {} existing", chatId, username);
        return userRepository.existsByChatIdOrUsername(chatId, username);
    }

    @Transactional
    public boolean existsByUsername(String username) {
        log.debug("checking if user with username {} existing", username);
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public boolean existsByChatId(Long chatId) {
        log.debug("checking if user with chatId {} existing", chatId);
        return userRepository.existsByChatId(chatId);
    }

    // remove
    public boolean removeByChatId(Long chatId) {
        log.debug("trying to remove user with chatId {}", chatId);
        return userRepository.removeByChatId(chatId);
    }

    public boolean removeById(Long id) {
        log.debug("trying to remove user with id {}", id);
        return userRepository.removeById(id);
    }
}

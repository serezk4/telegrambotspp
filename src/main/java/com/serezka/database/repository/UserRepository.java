package com.serezka.database.repository;

import com.serezka.database.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    long count();
    long countAllByRole(@NonNull User.Role role);

    User findByChatIdOrUsername(@NonNull Long chatId, @NonNull String username);
    User findByChatId(@NonNull Long chatId);
    User findByUsername(@NonNull String username);
    List<User> findAllByRole(@NonNull User.Role role);

    boolean existsByChatIdOrUsername(@NonNull Long chatId, @NonNull String username);
    boolean existsByChatId(@NonNull Long chatId);
    boolean existsByUsername(@NonNull String username);

    boolean removeByChatId(@NonNull Long chatId);
    boolean removeById(@NonNull Long id);
}

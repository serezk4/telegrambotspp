package com.serezka.database.repository;

import com.serezka.database.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    long count();
    long countAllByRole(@NonNull User.Role role);

    Optional<User> findByChatIdOrUsername(@NonNull Long chatId, @NonNull String username);
    Optional<User> findByChatId(@NonNull Long chatId);
    Optional<User> findByUsername(@NonNull String username);
    List<User> findAllByRole(@NonNull User.Role role);

    boolean existsByChatIdOrUsername(@NonNull Long chatId, @NonNull String username);
    boolean existsByChatId(@NonNull Long chatId);
    boolean existsByUsername(@NonNull String username);

    boolean removeByChatId(@NonNull Long chatId);
    boolean removeById(@NonNull Long id);
}

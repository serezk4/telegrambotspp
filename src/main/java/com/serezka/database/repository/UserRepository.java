package com.serezka.database.repository;

import com.serezka.database.model.DUser;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<DUser, Long> {
    long count();
    long countAllByRole(@NonNull DUser.Role role);

    Optional<DUser> findByChatIdOrUsername(@NonNull Long chatId, @NonNull String username);
    Optional<DUser> findByChatId(@NonNull Long chatId);
    Optional<DUser> findByUsername(@NonNull String username);
    List<DUser> findAllByRole(@NonNull DUser.Role role);

    boolean existsByChatIdOrUsername(@NonNull Long chatId, @NonNull String username);
    boolean existsByChatId(@NonNull Long chatId);
    boolean existsByUsername(@NonNull String username);

    boolean removeByChatId(@NonNull Long chatId);
    boolean removeById(@NonNull Long id);
}

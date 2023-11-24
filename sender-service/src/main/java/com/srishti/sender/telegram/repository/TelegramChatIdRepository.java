package com.srishti.sender.telegram.repository;

import com.srishti.sender.telegram.entity.TelegramChatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramChatIdRepository extends JpaRepository<TelegramChatId, Long> {

    Optional<TelegramChatId> findByTelegramId(String telegramId);
}

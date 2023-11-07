package com.srishti.notification.repository;

import com.srishti.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByIdAndOwnerId(Long notificationId, Long ownerId);
}

package com.srishti.notification.entity;

import com.srishti.notification.model.NotificationStatus;
import com.srishti.notification.model.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;

    private Long templateId;

    private Long subscriberId;

    private NotificationType type;

    private String credential;

    private String title;

    private String messageBody;

    @Builder.Default
    private NotificationStatus status = NotificationStatus.NEW;

    @Builder.Default
    private Integer retryAttempts = 0;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public Notification setNotificationStatus(NotificationStatus status) {
        setStatus(status);
        return this;
    }

    public Notification updateCreatedAt() {
        setCreatedAt(LocalDateTime.now());
        return this;
    }

    public Notification incrementRetryAttempts() {
        setRetryAttempts(retryAttempts + 1);
        return this;
    }
}

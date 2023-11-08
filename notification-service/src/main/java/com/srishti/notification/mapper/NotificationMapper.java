package com.srishti.notification.mapper;

import com.srishti.notification.dto.kafka.NotificationKafkaDto;
import com.srishti.notification.dto.request.NotificationRequest;
import com.srishti.notification.dto.response.NotificationResponse;
import com.srishti.notification.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper extends
        EntityMapper<Notification, NotificationRequest, NotificationResponse> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "retryAttempts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "subscriberId", ignore = true)
    @Mapping(target = "templateId", ignore = true)
    Notification mapToEntity(NotificationRequest request);

    @Override
    @Mapping(target = "messageBody", ignore = true)
    NotificationResponse mapToResponse(Notification notification);

    NotificationKafkaDto mapToKafka(NotificationResponse notificationResponse);
}

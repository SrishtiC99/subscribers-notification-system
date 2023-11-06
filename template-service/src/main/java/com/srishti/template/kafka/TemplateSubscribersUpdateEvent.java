package com.srishti.template.kafka;

import com.srishti.template.dto.kafka.Operation;
import com.srishti.template.dto.kafka.TemplateSubscribersUpdateDto;
import com.srishti.template.entity.SubscriberId;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class TemplateSubscribersUpdateEvent {

    private final KafkaTemplate<String, TemplateSubscribersUpdateDto> template;

    private String SUBSCRIBERS_UPDATE_TOPIC = "template-subscribers-updated";

    @PostRemove
    public void postRemove(SubscriberId subscriberId) {
        sendKafkaEvent(subscriberId, Operation.REMOVE);
    }

    @PostPersist
    public void postPersist(SubscriberId subscriberId) {
        sendKafkaEvent(subscriberId, Operation.PERSIST);
    }

    private void sendKafkaEvent(SubscriberId subscriberId, Operation operation) {
        System.out.println("sending event: ");
        template.send(SUBSCRIBERS_UPDATE_TOPIC,
                TemplateSubscribersUpdateDto.builder()
                        .operation(operation)
                        .templateId(subscriberId.getTemplate().getId())
                        .subscriberId(subscriberId.getSubscriberId())
                        .build());
    }
}

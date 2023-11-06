package com.srishti.subscriber.kafka;


import com.srishti.subscriber.dto.kafka.TemplateSubscribersUpdateDto;
import com.srishti.subscriber.entity.Subscriber;
import com.srishti.subscriber.entity.TemplateId;
import com.srishti.subscriber.repository.SubscriberRepository;
import com.srishti.subscriber.repository.TemplateIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final TemplateIdRepository templateIdRepository;
    private final SubscriberRepository subscriberRepository;

    @Transactional
    @KafkaListener(
            topics = "template-subscribers-updated",
            groupId = "subscriber-group",
            containerFactory = "templateSubscribersUpdateKafkaListenerFactory"
    )
    public void listener(TemplateSubscribersUpdateDto subscribersUpdateDto) {
        System.out.println("event received: " + subscribersUpdateDto.templateId());
        switch (subscribersUpdateDto.operation()) {
            case PERSIST -> {
                if(!templateIdRepository.existsByTemplateIdAndSubscriberId(
                        subscribersUpdateDto.templateId(),
                        subscribersUpdateDto.subscriberId()
                )) {
                    templateIdRepository.save(
                            TemplateId.builder()
                                    .subscriber(Subscriber
                                            .builder()
                                            .id(subscribersUpdateDto.subscriberId())
                                            .build())
                                    .templateId(subscribersUpdateDto.templateId())
                                    .build()
                    );
                }
            }
            case REMOVE -> {
                subscriberRepository.findById(subscribersUpdateDto.subscriberId())
                        .map(subscriber -> subscriber.removeTemplate(subscribersUpdateDto.templateId()))
                        .ifPresent(subscriberRepository::saveAndFlush);
            }
        }
    }
}

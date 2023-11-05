package com.srishti.subscriber.mapper;

import com.srishti.subscriber.dto.request.SubscriberRequest;
import com.srishti.subscriber.dto.response.SubscriberResponse;
import com.srishti.subscriber.entity.Subscriber;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriberMapper extends EntityMapper<Subscriber, SubscriberRequest, SubscriberResponse>{
}

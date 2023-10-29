package com.srishti.template.repository;

import com.srishti.template.entity.SubscriberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberIdRepository extends JpaRepository<SubscriberId, Long> {

    Boolean existsByTemplateIdAndSubscriberId(Long templateId, Long subscriberId);
}

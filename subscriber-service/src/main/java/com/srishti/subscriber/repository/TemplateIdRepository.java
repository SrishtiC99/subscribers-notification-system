package com.srishti.subscriber.repository;

import com.srishti.subscriber.entity.TemplateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateIdRepository extends JpaRepository<TemplateId, Long> {
    List<TemplateId> findAllBySubscriber_ownerIdAndTemplateId(Long clientId, Long templateId);

    Boolean existsByTemplateIdAndSubscriberId(Long templateId, Long subscriberId);
}

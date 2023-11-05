package com.srishti.subscriber.repository;

import com.srishti.subscriber.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

    Optional<Subscriber> findByEmailAndOwnerId(String email, Long ownerId);

    List<Subscriber> findAllByOwnerId(Long ownerId);

    Optional<Subscriber> findByIdAndOwnerId(Long subscriberId, Long ownerId);
}

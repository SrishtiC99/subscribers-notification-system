package com.srishti.template.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "templates",
        uniqueConstraints = {
                @UniqueConstraint(name = "templates_unq_ownerId-title", columnNames = {"ownerId", "title"})
        }
)
public class Template implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;

    private String title;

    private String content;

    private String imageUrl; //S3

    @ToString.Exclude
    @Builder.Default
    @OneToMany(
            mappedBy = "template",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SubscriberId> subscriberIds = new ArrayList<>();

    public Template addOwner(Long ownerId) {
        setOwnerId(ownerId);
        return this;
    }

    public void addSubscriber(Long subscriberId) {
        subscriberIds.add(
                SubscriberId.builder()
                        .subscriberId(subscriberId)
                        .template(this)
                        .build()
        );
    }

    public Template removeSubscriber(Long subscriberId) {
        subscriberIds.removeIf(
                subscriber -> Objects.equals(subscriber.getSubscriberId(), subscriberId)
        );
        return this;
    }
}

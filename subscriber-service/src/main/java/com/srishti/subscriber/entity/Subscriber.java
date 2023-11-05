package com.srishti.subscriber.entity;

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
        name = "subscribers",
        uniqueConstraints = {
                @UniqueConstraint(name = "subscribers_unq_ownerId-email", columnNames = {"ownerId", "email"}),
                @UniqueConstraint(name = "subscribers_unq_ownerId-phoneNumber", columnNames = {"ownerId", "phoneNumber"}),
                @UniqueConstraint(name = "subscribers_unq_ownerId-telegramId", columnNames = {"ownerId", "telegramId"})
        },
        indexes = {
                @Index(name = "subscribers_idx_email", columnList = "email")
        }
)
public class Subscriber implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;

    private String name;

    private Geolocation geolocation;

    @Column(nullable = false)
    private String email;

    private String phoneNumber;

    private String telegramId;

    @ToString.Exclude
    @Builder.Default
    @OneToMany(
            mappedBy = "subscriber",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TemplateId> templateIds = new ArrayList<>();

    public Subscriber addOwner(Long ownerId) {
        setOwnerId(ownerId);
        return this;
    }

    public void addTemplate(Long templateId) {
        templateIds.add(
                TemplateId.builder()
                        .templateId(templateId)
                        .subscriber(this)
                        .build()
        );
    }

    public Subscriber removeTemplate(Long templateId) {
        templateIds.removeIf(
                template -> Objects.equals(template.getTemplateId(), templateId)
        );
        return this;
    }
}

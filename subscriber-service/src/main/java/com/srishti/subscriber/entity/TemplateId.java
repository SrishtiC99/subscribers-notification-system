package com.srishti.subscriber.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "template_ids",
        uniqueConstraints = {
                @UniqueConstraint(name = "template_ids_unq_subscriber-template", columnNames = {"subscriber_id", "templateId"})
        }
)
public class TemplateId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Subscriber subscriber;

    private Long templateId;
}

package com.srishti.template.entity;

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
        name = "subscriber_ids",
        uniqueConstraints = {
                @UniqueConstraint(name = "subscriber_ids_unq_template-subscriber",
                        columnNames = {"template_id", "subscriberId"})
        }
)
public class SubscriberId implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Template template;

    private Long subscriberId;
}

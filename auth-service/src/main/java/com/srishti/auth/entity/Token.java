package com.srishti.auth.entity;


import com.srishti.auth.model.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.srishti.auth.model.TokenType.BEARER;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token implements BaseEntity<Long>{

    @Id
    @GeneratedValue
    public Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    public User user;

    @Enumerated(EnumType.STRING)
    public final TokenType tokenType = BEARER;

    @Column(unique = true)
    public String jwt;

    public Boolean isRevoked;

    public Boolean isExpired;
}

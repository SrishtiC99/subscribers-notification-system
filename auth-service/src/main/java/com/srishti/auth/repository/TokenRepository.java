package com.srishti.auth.repository;

import com.srishti.auth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByUser_Id(Long id);

    Optional<Token> findByJwt(String jwt);
}

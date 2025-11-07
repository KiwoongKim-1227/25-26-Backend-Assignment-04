package com.example.jwtexample.repository;

import com.example.jwtexample.domain.RefreshToken;
import com.example.jwtexample.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(UserAccount user);
}

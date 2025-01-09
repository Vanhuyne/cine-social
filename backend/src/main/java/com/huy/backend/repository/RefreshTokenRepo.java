package com.huy.backend.repository;

import com.huy.backend.models.RefreshToken;
import com.huy.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken , Long> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
    void deleteByExpiryDateBefore (Instant now);
}

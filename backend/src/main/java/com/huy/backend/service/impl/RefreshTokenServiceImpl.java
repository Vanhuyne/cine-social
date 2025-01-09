package com.huy.backend.service.impl;

import com.huy.backend.exception.TokenRefreshException;
import com.huy.backend.models.RefreshToken;
import com.huy.backend.models.User;
import com.huy.backend.repository.RefreshTokenRepo;
import com.huy.backend.repository.UserRepo;
import com.huy.backend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${jwt.refresh.token.expiration}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepo refreshTokenRepository;
    private final UserRepo userRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createOrUpdateRefreshToken(User user) {
        refreshTokenRepository.findByUser(user)
                .ifPresent(token -> refreshTokenRepository.delete(token));

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .token(UUID.randomUUID().toString())
                .build();


        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired. Please login again");
        }
        return token;
    }

    @Override
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    // Optional: Periodic cleanup of expired tokens
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) // Run once a day
    public void removeExpiredTokens() {
        refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
    }
}

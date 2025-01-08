package com.huy.backend.service;

import com.huy.backend.models.RefreshToken;
import com.huy.backend.models.User;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createOrUpdateRefreshToken(User user);
    Optional<RefreshToken> findByToken(String token);
    RefreshToken verifyExpiration(RefreshToken token);
    void deleteByUser(User user);
}

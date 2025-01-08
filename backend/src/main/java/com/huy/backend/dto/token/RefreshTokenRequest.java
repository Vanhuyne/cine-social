package com.huy.backend.dto.token;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
package com.huy.backend.dto.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateUserResponse {
    private String message;
    private String accessToken;
    private String refreshToken;
}

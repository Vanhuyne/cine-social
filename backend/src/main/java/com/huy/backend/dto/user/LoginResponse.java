package com.huy.backend.dto.user;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LoginResponse {
    private String message;
    private String accessToken;
    private String refreshToken;
}

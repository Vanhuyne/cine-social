package com.huy.backend.dto.user;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserRegistrationResponseDTO {
    private Long userId;
    private String username;
    private String email;
}

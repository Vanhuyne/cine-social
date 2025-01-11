package com.huy.backend.service;

import com.huy.backend.dto.user.*;

public interface UserService {
    UserRegistrationResponseDTO register(UserRegister userRegister);
    LoginResponse login (LoginRequest loginRequest);
}

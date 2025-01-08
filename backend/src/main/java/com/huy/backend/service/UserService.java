package com.huy.backend.service;

import com.huy.backend.dto.user.LoginRequest;
import com.huy.backend.dto.user.LoginResponse;
import com.huy.backend.dto.user.UserDTO;
import com.huy.backend.dto.user.UserRegister;

public interface UserService {
    UserDTO register(UserRegister userRegister);
    LoginResponse login (LoginRequest loginRequest);
}

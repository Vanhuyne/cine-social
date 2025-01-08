package com.huy.backend.controller;

import com.huy.backend.dto.token.TokenRefreshRequest;
import com.huy.backend.dto.token.TokenRefreshResponse;
import com.huy.backend.dto.user.LoginRequest;
import com.huy.backend.dto.user.LoginResponse;
import com.huy.backend.dto.user.UserDTO;
import com.huy.backend.dto.user.UserRegister;
import com.huy.backend.exception.TokenRefreshException;
import com.huy.backend.models.RefreshToken;
import com.huy.backend.security.JwtUtil;
import com.huy.backend.service.RefreshTokenService;
import com.huy.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserRegister userRegister) {
        UserDTO userDTO = userService.register(userRegister);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = userService.login(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtUtil.generateToken(user.getUsername());
                    String refreshToken = refreshTokenService.createOrUpdateRefreshToken(user).getToken();
                    return ResponseEntity.ok(new TokenRefreshResponse(accessToken, refreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException("Refresh token not found!"));
    }


}

package com.huy.backend.controller;

import com.huy.backend.dto.google.GoogleTokenResponse;
import com.huy.backend.dto.google.GoogleUserInfo;
import com.huy.backend.dto.token.TokenRefreshRequest;
import com.huy.backend.dto.token.TokenRefreshResponse;
import com.huy.backend.dto.user.*;
import com.huy.backend.exception.TokenRefreshException;
import com.huy.backend.models.RefreshToken;
import com.huy.backend.models.User;
import com.huy.backend.security.JwtUtil;
import com.huy.backend.service.RefreshTokenService;
import com.huy.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final WebClient webClient = WebClient.create();

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDTO> register(@Valid @RequestBody UserRegister userRegister) {
        UserRegistrationResponseDTO userDTO = userService.register(userRegister);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRoles());
                    String refreshToken = refreshTokenService.createOrUpdateRefreshToken(user).getToken();
                    return ResponseEntity.ok(new TokenRefreshResponse(accessToken, refreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException("Refresh token not found!"));
    }

    // get current user
    @PostMapping("/current-user")
    public ResponseEntity<UserProfileDTO> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PostMapping("/google")
    public ResponseEntity<?> authenticateGoogle(@RequestBody Map<String, String> payload) {
        String code = payload.get("code");
        GoogleTokenResponse tokenResponse = webClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("code=" + code +
                        "&client_id=" + clientId +
                        "&client_secret=" + clientSecret +
                        "&redirect_uri=" + redirectUri +
                        "&grant_type=authorization_code"
                )
                .retrieve()
                .bodyToMono(GoogleTokenResponse.class)
                .block();

        // Fetch user info
        GoogleUserInfo userInfo = webClient.get()
                .uri("https://www.googleapis.com/oauth2/v3/userinfo")
                .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .retrieve()
                .bodyToMono(GoogleUserInfo.class)
                .block();
        // get user by email
        User user = userService.getUserByEmail(userInfo.getEmail());
        
        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRoles());
        String refreshToken = refreshTokenService.createOrUpdateRefreshToken(user).getToken();


        return ResponseEntity.ok(
                new LoginResponse("Login successful", accessToken, refreshToken)
        );
    }
}

package com.huy.backend.service;

import com.huy.backend.dto.user.LoginRequest;
import com.huy.backend.dto.user.LoginResponse;
import com.huy.backend.dto.user.UserRegister;
import com.huy.backend.dto.user.UserRegistrationResponseDTO;
import com.huy.backend.exception.UserAlreadyExistsException;
import com.huy.backend.models.RefreshToken;
import com.huy.backend.models.User;
import com.huy.backend.repository.UserRepo;
import com.huy.backend.security.JwtUtil;
import com.huy.backend.service.impl.RefreshTokenServiceImpl;
import com.huy.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestUserService {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegister userRegister;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenServiceImpl refreshTokenService;

    @BeforeEach
    public void setUp() {
        userRegister = UserRegister.builder()
                .username("testUsername")
                .email("testEmail")
                .password("testPassword")
                .build();
    }

    @Test
    void register_ShouldThrowException_WhenUsernameExists() {
        // Given
        when(userRepo.existsByUsername("testUsername")).thenReturn(true);

        // THEN
        assertThrows(UserAlreadyExistsException.class, () -> {
            // Action
            userService.register(userRegister);
        });
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        // Given
        when(userRepo.existsByEmail("testEmail")).thenReturn(true);

        // Action
        assertThrows(UserAlreadyExistsException.class, () -> {
            // Action
            userService.register(userRegister);
        });
    }

    @Test
    void register_ShouldSaveUser_WhenUsernameAndEmailDoNotExist() {
        // Given
        when(userRepo.existsByUsername("testUsername")).thenReturn(false);
        when(userRepo.existsByEmail("testEmail")).thenReturn(false);
        when(userRepo.save(any(User.class))).thenReturn(new User());

        // Action
        UserRegistrationResponseDTO response = userService.register(userRegister);

        // Assert
        assertNotNull(response);
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void login_ShouldThrowException_WhenAccountDoesNotExist() {
        // Given
        when(userRepo.findByUsernameOrEmail("testUsername")).thenReturn(Optional.empty());

        // Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            // Action
            userService.login(new LoginRequest("testUsername", "testPassword"));
        });
    }

    @Test
    void login_ShouldReturnLoginResponse_WhenCredentialsAreCorrect() {
        // Given
        User user = new User();
        user.setUsername("testUsername");
        user.setRoles(Set.of("ROLE_USER"));
        when(userRepo.findByUsernameOrEmail("testUsername")).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, "testPassword", Collections.emptyList()));
        when(jwtUtil.generateToken(user.getUsername(), user.getRoles())).thenReturn("accessToken");
        when(refreshTokenService.createOrUpdateRefreshToken(any(User.class))).thenReturn(new RefreshToken(1L, user, "refreshToken", Instant.now()));

        // Action
        LoginResponse response = userService.login(new LoginRequest("testUsername", "testPassword"));

        // Assert
        assertNotNull(response);
        assertEquals("Login successful", response.getMessage());
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    void login_ShouldThrowBadCredentialsException_WhenPasswordIsIncorrect() {
        // Given
        User user = new User();
        user.setUsername("testUsername");
        user.setRoles(Set.of("ROLE_USER"));

        when(userRepo.findByUsernameOrEmail(user.getUsername())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Password is incorrect"));

        // Assert
        assertThrows(BadCredentialsException.class, () -> {
            // Action
            userService.login(new LoginRequest("testUsername", "testPassword"));
        });
    }
}
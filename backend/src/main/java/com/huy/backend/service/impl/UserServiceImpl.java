package com.huy.backend.service.impl;

import com.huy.backend.dto.user.LoginRequest;
import com.huy.backend.dto.user.LoginResponse;
import com.huy.backend.dto.user.UserDTO;
import com.huy.backend.dto.user.UserRegister;
import com.huy.backend.exception.UserAlreadyExistsException;
import com.huy.backend.models.RefreshToken;
import com.huy.backend.models.Roles;
import com.huy.backend.models.User;
import com.huy.backend.repository.UserRepo;
import com.huy.backend.security.JwtUtil;
import com.huy.backend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenServiceImpl refreshTokenService;

    @Override
    @Transactional
    public UserDTO register(UserRegister userRegister) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(userRegister.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(userRegister.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        // Create new user
        User user = mapRegisterToUser(userRegister);
        User savedUser = userRepository.save(user);

        return mapUserToDTO(savedUser);
    }

    @Override
    public LoginResponse login (LoginRequest loginRequest) {
        // Check if account exists
        if (userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail()).isEmpty()) {
                throw new UsernameNotFoundException("Account does not exist");
        }
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );
            // Set the authentication in the context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            // generate JWT token
            String accessToken = jwtUtil.generateToken(loginRequest.getUsernameOrEmail());
            String refreshToken = refreshTokenService.createOrUpdateRefreshToken(user).getToken();
            return new LoginResponse("Login successful", accessToken, refreshToken);
        } catch (BadCredentialsException  e) {
            throw new BadCredentialsException("Password is incorrect");
        }
    }


    private User mapRegisterToUser(UserRegister userRegister) {
        return User.builder()
                .username(userRegister.getUsername())
                .email(userRegister.getEmail())
                .password(passwordEncoder.encode(userRegister.getPassword()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .roles(Collections.singleton(Roles.ROLE_USER.name()))
                .build();
    }

    private UserDTO mapUserToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

package com.huy.backend.service;

import com.huy.backend.dto.user.*;
import com.huy.backend.exception.ResourceNotFoundException;
import com.huy.backend.exception.UserAlreadyExistsException;
import com.huy.backend.models.Roles;
import com.huy.backend.models.User;
import com.huy.backend.repository.UserRepo;
import com.huy.backend.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final FileStorageService fileStorageService;

    @Transactional
    public UserRegistrationResponseDTO register(UserRegister userRegister) {
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

        return mapUserToResponseDTO(savedUser);
    }

    public LoginResponse login(LoginRequest loginRequest) {
        // Check if account exists
        if (userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail()).isEmpty()) {
            throw new UsernameNotFoundException("Account does not exist");
        }
        try {
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
            String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRoles());
            String refreshToken = refreshTokenService.createOrUpdateRefreshToken(user).getToken();
            return new LoginResponse("Login successful", accessToken, refreshToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Password is incorrect");
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByUsernameOrEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public UserProfileDTO getCurrentUser() {
        User user = getAuthCurrent();
        log.info("User: {}", user.getEmail());
        return mapUserToDTO(user);
    }

    public User getAuthCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Auth: {}", authentication.getName());
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UpdateUserResponse updateProfile(UpdateUserRequest updateUserRequest) {
        User user = getAuthCurrent();

        // validate email
        if (!user.getEmail().equals(updateUserRequest.getEmail()) && userRepository.existsByEmail(updateUserRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        // validate username
        if (!user.getUsername().equals(updateUserRequest.getUsername()) && userRepository.existsByUsername(updateUserRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        // Handle profile picture upload
        MultipartFile profilePicture = updateUserRequest.getProfilePicture();
        if (profilePicture != null && !profilePicture.isEmpty()) {
            String profilePicturePath = fileStorageService.storeFile(profilePicture);

            if (user.getProfilePicture() != null) {
                fileStorageService.deleteFile(user.getProfilePicture());
            }
            user.setProfilePicture(profilePicturePath);
        }

        user.setUsername(updateUserRequest.getUsername());
        user.setEmail(updateUserRequest.getEmail());
        user.setUpdatedAt(LocalDateTime.now());

        User updateUser = userRepository.save(user);
        return UpdateUserResponse.builder()
                .message("Profile updated successfully")
                .accessToken(jwtUtil.generateToken(updateUser.getUsername(), updateUser.getRoles()))
                .refreshToken(refreshTokenService.createOrUpdateRefreshToken(updateUser).getToken())
                .build();
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

    private UserProfileDTO mapUserToDTO(User user) {
        return UserProfileDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private UserRegistrationResponseDTO mapUserToResponseDTO(User user) {
        return UserRegistrationResponseDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

}

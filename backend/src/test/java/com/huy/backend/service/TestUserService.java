package com.huy.backend.service;

import com.huy.backend.dto.user.UserRegister;
import com.huy.backend.exception.UserAlreadyExistsException;
import com.huy.backend.repository.UserRepo;
import com.huy.backend.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TestUserService {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    // before testing
     @BeforeEach
    public void setUp() {
         MockitoAnnotations.initMocks(this);
     }

     @Test
    void register_ShouldThrowException_WhenUsernameExists() {
         // Given
         UserRegister userRegister = new UserRegister("existingUser", "email@example.com", "password");
         when(userRepo.existsByUsername("existingUser")).thenReturn(true);

         // When & Then
         UserAlreadyExistsException exception = assertThrows(
                 UserAlreadyExistsException.class,
                 () -> userService.register(userRegister)
         );

         assertEquals("Username already exists", exception.getMessage());
     }
}

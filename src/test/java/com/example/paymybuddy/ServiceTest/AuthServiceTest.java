package com.example.paymybuddy.service;

import com.example.paymybuddy.dto.AuthReponse;
import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_PASSWORD = "password123";
    private final String ENCODED_PASSWORD = "encodedPassword123";
    private final String JWT_TOKEN = "jwt.token.here";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setMail(TEST_EMAIL);
        testUser.setPassword(TEST_PASSWORD);
    }

    @Test
    void register_ShouldRegisterNewUser_WhenEmailNotExists() {
        // Arrange
        when(userRepository.existsByMail(TEST_EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(jwtService.generateToken(any(User.class), eq(TEST_EMAIL))).thenReturn(JWT_TOKEN);

        // Act
        AuthReponse response = authService.register(testUser);

        // Assert
        assertEquals(JWT_TOKEN, response.getToken());
        assertEquals("User registered successfully", response.getMessage());

        verify(userRepository).existsByMail(TEST_EMAIL);
        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(userRepository).save(testUser);
        verify(jwtService).generateToken(testUser, TEST_EMAIL);

        assertEquals(ENCODED_PASSWORD, testUser.getPassword());
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        // Arrange
        when(userRepository.existsByMail(TEST_EMAIL)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.register(testUser)
        );

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository).existsByMail(TEST_EMAIL);
        verify(userRepository, never()).save(any(User.class));
        verify(jwtService, never()).generateToken(any(User.class), anyString());
    }

    @Test
    void loginUser_ShouldLoginUser_WhenCredentialsAreValid() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByMail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(testUser, TEST_EMAIL)).thenReturn(JWT_TOKEN);

        // Act
        AuthReponse response = authService.loginUser(TEST_EMAIL, TEST_PASSWORD);

        // Assert
        assertEquals(JWT_TOKEN, response.getToken());
        assertEquals("Login successful", response.getMessage());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByMail(TEST_EMAIL);
        verify(jwtService).generateToken(testUser, TEST_EMAIL);
    }

    @Test
    void loginUser_ShouldThrowException_WhenCredentialsAreInvalid() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.loginUser(TEST_EMAIL, TEST_PASSWORD)
        );

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findByMail(anyString());
        verify(jwtService, never()).generateToken(any(User.class), anyString());
    }

    @Test
    void loginUser_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByMail(TEST_EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.loginUser(TEST_EMAIL, TEST_PASSWORD)
        );

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByMail(TEST_EMAIL);
        verify(jwtService, never()).generateToken(any(User.class), anyString());
    }
}
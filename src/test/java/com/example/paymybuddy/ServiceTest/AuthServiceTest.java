package com.example.paymybuddy.ServiceTest;

import com.example.paymybuddy.dto.AuthResponse;
import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserRepository;
import com.example.paymybuddy.service.AuthService;
import com.example.paymybuddy.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
    private final String TEST_TOKEN = "test-jwt-token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Réinitialiser le SecurityContext avant chaque test
        SecurityContextHolder.clearContext();

        // Configuration de l'utilisateur de test
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setMail("test@example.com");
        testUser.setPassword("rawPassword");
        testUser.setRole("USER");
        testUser.setBalance(100.0);

        // Configuration des mocks par défaut
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn(TEST_TOKEN);
    }

    @Test
    void register_Success_ShouldReturnAuthResponse() {
        // Arrange
        when(userRepository.existsByMail(anyString())).thenReturn(false);

        // Act
        AuthResponse response = authService.register(testUser);

        // Assert
        assertNotNull(response);
        assertEquals(TEST_TOKEN, response.getToken());
        assertEquals("User registered successfully", response.getMessage());

        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(testUser);
        verify(jwtService).generateToken(testUser);

        // Vérifier que le mot de passe a été encodé
        assertEquals("encodedPassword", testUser.getPassword());
    }

    @Test
    void register_EmailAlreadyExists_ShouldThrowException() {
        // Arrange
        when(userRepository.existsByMail("test@example.com")).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.register(testUser);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository).existsByMail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    void loginUser_Success_ShouldReturnAuthResponse() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByMail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        AuthResponse response = authService.loginUser("test@example.com", "password123");

        // Assert
        assertNotNull(response);
        assertEquals(TEST_TOKEN, response.getToken());
        assertEquals("Login successful", response.getMessage());

        // Vérifier que l'authentification a été effectuée
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("test@example.com", "password123"));
        verify(userRepository).findByMail("test@example.com");
        verify(jwtService).generateToken(testUser);

        // Vérifier que le SecurityContext a été mis à jour
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void loginUser_InvalidCredentials_ShouldThrowException() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.loginUser("test@example.com", "wrongPassword");
        });

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("test@example.com", "wrongPassword"));
        verify(userRepository, never()).findByMail(anyString());
        verify(jwtService, never()).generateToken(any(User.class));

        // Vérifier que le SecurityContext n'a pas été mis à jour
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void loginUser_UserNotFound_ShouldThrowException() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByMail("test@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.loginUser("test@example.com", "password123");
        });

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("test@example.com", "password123"));
        verify(userRepository).findByMail("test@example.com");
        verify(jwtService, never()).generateToken(any(User.class));
    }
}
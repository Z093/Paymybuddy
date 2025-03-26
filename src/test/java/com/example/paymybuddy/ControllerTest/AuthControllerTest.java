package com.example.paymybuddy.ControllerTest;

import com.example.paymybuddy.controller.AuthController;
import com.example.paymybuddy.dto.AuthReponse;
import com.example.paymybuddy.model.User;
import com.example.paymybuddy.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showRegisterForm_ShouldReturnRegisterView() {
        // Arrange & Act
        String viewName = authController.showRegisterForm(model);

        // Assert
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
    }

    @Test
    void showLoginForm_ShouldReturnLoginView() {
        // Arrange & Act
        String viewName = authController.showLoginForm(model);

        // Assert
        assertEquals("login", viewName);
        verify(model).addAttribute(eq("user"), any(User.class));
    }

    @Test
    void registerUser_Success_ShouldRedirectToLogin() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setMail("test@example.com");
        user.setPassword("password123");

        AuthReponse mockResponse = new AuthReponse();
        mockResponse.setToken("test-token");
        when(authService.register(any(User.class))).thenReturn(mockResponse);

        // Act
        String viewName = authController.registerUser(user, model);

        // Assert
        assertEquals("redirect:/api/v1/login", viewName);
        verify(authService).register(any(User.class));
        verifyNoInteractions(model); // No error should be added to model
    }

    @Test
    void registerUser_Failure_ShouldReturnRegisterViewWithError() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setMail("test@example.com");
        user.setPassword("password123");

        String errorMessage = "Email already exists";
        when(authService.register(any(User.class))).thenThrow(new RuntimeException(errorMessage));

        // Act
        String viewName = authController.registerUser(user, model);

        // Assert
        assertEquals("register", viewName);
        verify(model).addAttribute("error", errorMessage);
    }

    @Test
    void loginUser_Success_ShouldRedirectToTransfer() {
        // Arrange
        String mail = "test@example.com";
        String password = "password123";

        AuthReponse mockResponse = new AuthReponse();
        mockResponse.setToken("test-token");
        when(authService.loginUser(mail, password)).thenReturn(mockResponse);

        // Act
        String viewName = authController.loginUser(mail, password, session, model);

        // Assert
        assertEquals("redirect:/api/v1/transfer", viewName);
        verify(authService).loginUser(mail, password);
        verify(session).setAttribute("token", "test-token");
        verifyNoInteractions(model); // No error should be added to model
    }

    @Test
    void loginUser_Failure_ShouldReturnLoginViewWithError() {
        // Arrange
        String mail = "test@example.com";
        String password = "wrong-password";

        String errorMessage = "Invalid credentials";
        when(authService.loginUser(mail, password)).thenThrow(new RuntimeException(errorMessage));

        // Act
        String viewName = authController.loginUser(mail, password, session, model);

        // Assert
        assertEquals("login", viewName);
        verify(model).addAttribute("error", errorMessage);
        verifyNoInteractions(session); // Session should not be modified
    }
}
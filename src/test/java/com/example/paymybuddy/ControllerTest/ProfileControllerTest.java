package com.example.paymybuddy.ControllerTest;

import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserRepository;
import com.example.paymybuddy.controller.ProfileController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private ProfileController profileController;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuration du Principal mock
        when(principal.getName()).thenReturn("test@example.com");

        // Création d'un utilisateur de test
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setMail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole("USER");
        testUser.setBalance(100.0);
    }

    @Test
    void showProfile_UserFound_ShouldReturnProfileView() {
        // Arrange
        when(userRepository.findByMail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        String viewName = profileController.showProfile(model, principal);

        // Assert
        assertEquals("profile", viewName);
        verify(model).addAttribute("user", testUser);
        verify(userRepository).findByMail("test@example.com");
    }

    @Test
    void showProfile_UserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByMail("test@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            profileController.showProfile(model, principal);
        });
        verify(userRepository).findByMail("test@example.com");
    }

    @Test
    void updateUser_WithPasswordChange_ShouldUpdateAndRedirect() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");
        updatedUser.setMail("updated@example.com");
        updatedUser.setPassword("newPassword");

        when(userRepository.findByMail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        // Act
        String result = profileController.updateUser(updatedUser, principal, redirectAttributes);

        // Assert
        assertEquals("redirect:/api/v1/profile", result);
        verify(userRepository).findByMail("test@example.com");
        verify(userRepository).save(testUser);
        verify(passwordEncoder).encode("newPassword");
        verify(redirectAttributes).addFlashAttribute("message", "Profil mis à jour avec succès !");

        // Vérifier que les propriétés de l'utilisateur ont été mises à jour
        assertEquals("updatedUsername", testUser.getUsername());
        assertEquals("updated@example.com", testUser.getMail());
        assertEquals("encodedNewPassword", testUser.getPassword());
    }

    @Test
    void updateUser_WithoutPasswordChange_ShouldNotEncodePassword() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");
        updatedUser.setMail("updated@example.com");
        updatedUser.setPassword(""); // Pas de changement de mot de passe

        when(userRepository.findByMail("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        String result = profileController.updateUser(updatedUser, principal, redirectAttributes);

        // Assert
        assertEquals("redirect:/api/v1/profile", result);
        verify(userRepository).findByMail("test@example.com");
        verify(userRepository).save(testUser);
        verify(passwordEncoder, never()).encode(anyString()); // Vérifier que l'encodeur n'a jamais été appelé
        verify(redirectAttributes).addFlashAttribute("message", "Profil mis à jour avec succès !");

        // Vérifier que les propriétés de l'utilisateur ont été mises à jour (sauf le mot de passe)
        assertEquals("updatedUsername", testUser.getUsername());
        assertEquals("updated@example.com", testUser.getMail());
        assertEquals("encodedPassword", testUser.getPassword()); // Le mot de passe reste inchangé
    }

    @Test
    void updateUser_UserNotFound_ShouldRedirectWithError() {
        // Arrange
        User updatedUser = new User();
        when(userRepository.findByMail("test@example.com")).thenReturn(Optional.empty());

        // Act
        String result = profileController.updateUser(updatedUser, principal, redirectAttributes);

        // Assert
        assertEquals("redirect:/api/v1/profile?error", result);
        verify(userRepository).findByMail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(redirectAttributes); // Pas d'interaction avec redirectAttributes
    }
}
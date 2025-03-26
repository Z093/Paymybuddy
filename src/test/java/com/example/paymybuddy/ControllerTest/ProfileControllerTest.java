package com.example.paymybuddy.ControllerTest;

import com.example.paymybuddy.controller.ProfileController;
import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProfileControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private ProfileController profileController;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Configuration du SecurityContext avec l'authentification mockée
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Création d'un utilisateur de test
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setMail("test@example.com");
        testUser.setPassword("encodedPassword");
    }

    @Test
    public void testShowProfile_WithUserPrincipal() {
        // Configuration du mock pour le cas où authentication.getPrincipal() retourne un User
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.findByMail(testUser.getMail())).thenReturn(Optional.of(testUser));

        // Exécution de la méthode à tester
        String viewName = profileController.showProfile(model);

        // Vérifications
        verify(model, times(1)).addAttribute("user", testUser);
        assertEquals("profile", viewName);
    }

    @Test
    public void testShowProfile_WithStringPrincipal() {
        // Configuration du mock pour le cas où authentication.getPrincipal() retourne un String
        when(authentication.getPrincipal()).thenReturn("not_a_user_object");
        when(authentication.getName()).thenReturn(testUser.getMail());
        when(userRepository.findByMail(testUser.getMail())).thenReturn(Optional.of(testUser));

        // Exécution de la méthode à tester
        String viewName = profileController.showProfile(model);

        // Vérifications
        verify(model, times(1)).addAttribute("user", testUser);
        assertEquals("profile", viewName);
    }

    @Test
    public void testShowProfile_UserNotFound() {
        // Configuration du mock
        when(authentication.getPrincipal()).thenReturn("not_a_user_object");
        when(authentication.getName()).thenReturn("nonexistent@example.com");
        when(userRepository.findByMail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Vérification que l'exception est lancée
        assertThrows(UsernameNotFoundException.class, () -> {
            profileController.showProfile(model);
        });
    }

    @Test
    public void testUpdateUser_Success() {
        // Utilisateur mis à jour
        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");
        updatedUser.setMail("updated@example.com");
        updatedUser.setPassword("newPassword");

        // Configuration du mock
        when(authentication.getPrincipal()).thenReturn("not_a_user_object");
        when(authentication.getName()).thenReturn(testUser.getMail());
        when(userRepository.findByMail(testUser.getMail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        // Exécution de la méthode à tester
        String viewName = profileController.updateUser(updatedUser, redirectAttributes);

        // Vérifications
        verify(userRepository, times(1)).save(testUser);
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "Profil mis à jour avec succès !");
        assertEquals("redirect:/api/v1/profile", viewName);
        assertEquals("updatedUsername", testUser.getUsername());
        assertEquals("updated@example.com", testUser.getMail());
        assertEquals("encodedNewPassword", testUser.getPassword());
    }

    @Test
    public void testUpdateUser_EmptyPassword() {
        // Utilisateur mis à jour avec mot de passe vide
        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");
        updatedUser.setMail("updated@example.com");
        updatedUser.setPassword(""); // Mot de passe vide

        // Configuration du mock
        when(authentication.getPrincipal()).thenReturn("not_a_user_object");
        when(authentication.getName()).thenReturn(testUser.getMail());
        when(userRepository.findByMail(testUser.getMail())).thenReturn(Optional.of(testUser));

        // Exécution de la méthode à tester
        String viewName = profileController.updateUser(updatedUser, redirectAttributes);

        // Vérifications
        verify(userRepository, times(1)).save(testUser);
        verify(passwordEncoder, never()).encode(anyString()); // Vérifier que l'encodeur n'est pas appelé
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "Profil mis à jour avec succès !");
        assertEquals("redirect:/api/v1/profile", viewName);
        assertEquals("updatedUsername", testUser.getUsername());
        assertEquals("updated@example.com", testUser.getMail());
        assertEquals("encodedPassword", testUser.getPassword()); // Le mot de passe reste inchangé
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        // Utilisateur mis à jour
        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");
        updatedUser.setMail("updated@example.com");
        updatedUser.setPassword("newPassword");

        // Configuration du mock pour un utilisateur non trouvé
        when(authentication.getPrincipal()).thenReturn("not_a_user_object");
        when(authentication.getName()).thenReturn("nonexistent@example.com");
        when(userRepository.findByMail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Exécution de la méthode à tester
        String viewName = profileController.updateUser(updatedUser, redirectAttributes);

        // Vérifications
        verify(userRepository, never()).save(any());
        assertEquals("redirect:/api/v1/profile?error", viewName);
    }

    @Test
    public void testUpdateUser_WithUserPrincipal() {
        // Utilisateur mis à jour
        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");
        updatedUser.setMail("updated@example.com");
        updatedUser.setPassword("newPassword");

        // Configuration du mock pour le cas où authentication.getPrincipal() retourne un User
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(userRepository.findByMail(testUser.getMail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        // Exécution de la méthode à tester
        String viewName = profileController.updateUser(updatedUser, redirectAttributes);

        // Vérifications
        verify(userRepository, times(1)).save(testUser);
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "Profil mis à jour avec succès !");
        assertEquals("redirect:/api/v1/profile", viewName);
    }
}
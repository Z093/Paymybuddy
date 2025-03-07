package com.example.paymybuddy.ControllerTest;

import com.example.paymybuddy.controller.UserFriendsController;
import com.example.paymybuddy.service.UserFriendsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserFriendsControllerTest {

    @Mock
    private UserFriendsService userFriendsService;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserFriendsController userFriendsController;

    private final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuration du SecurityContext mockito
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Configuration de l'authentification mockito
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Configuration des UserDetails mockito
        when(userDetails.getUsername()).thenReturn(TEST_EMAIL);
    }

    @Test
    void showAddFriendPage_AuthenticatedUser_ShouldReturnAddFriendsView() {
        // Act
        String viewName = userFriendsController.showAddFriendPage(model);

        // Assert
        assertEquals("addFriends", viewName);
        verify(model).addAttribute("userEmail", TEST_EMAIL);
    }

    @Test
    void showAddFriendPage_UnauthenticatedUser_ShouldUseAnonymous() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn("anonymousPrincipal"); // Non UserDetails

        // Act
        String viewName = userFriendsController.showAddFriendPage(model);

        // Assert
        assertEquals("addFriends", viewName);
        verify(model).addAttribute("userEmail", "anonymous");
    }

    @Test
    void showAddFriendPage_NullAuthentication_ShouldUseAnonymous() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        String viewName = userFriendsController.showAddFriendPage(model);

        // Assert
        assertEquals("addFriends", viewName);
        verify(model).addAttribute("userEmail", "anonymous");
    }

    @Test
    void addFriend_Success_ShouldReturnSuccessMessage() {
        // Arrange
        String friendMail = "friend@example.com";

        // Act
        String viewName = userFriendsController.addFriend(TEST_EMAIL, friendMail, model);

        // Assert
        assertEquals("addFriends", viewName);
        verify(userFriendsService).addFriend(TEST_EMAIL, friendMail);
        verify(model).addAttribute("message", "Ami ajouté avec succès !");
    }

    @Test
    void addFriend_Failure_ShouldReturnErrorMessage() {
        // Arrange
        String friendMail = "friend@example.com";
        String errorMessage = "L'utilisateur n'existe pas";
        doThrow(new RuntimeException(errorMessage))
                .when(userFriendsService).addFriend(anyString(), anyString());

        // Act
        String viewName = userFriendsController.addFriend(TEST_EMAIL, friendMail, model);

        // Assert
        assertEquals("addFriends", viewName);
        verify(userFriendsService).addFriend(TEST_EMAIL, friendMail);
        verify(model).addAttribute("message", "Erreur : " + errorMessage);
    }
}
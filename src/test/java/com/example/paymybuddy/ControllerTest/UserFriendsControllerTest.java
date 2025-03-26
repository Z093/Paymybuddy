package com.example.paymybuddy.ControllerTest;

import com.example.paymybuddy.controller.UserFriendsController;
import com.example.paymybuddy.model.User;
import com.example.paymybuddy.service.UserFriendsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @InjectMocks
    private UserFriendsController userFriendsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testShowAddFriendPage_AuthenticatedUser() {
        // Arrange
        User mockUser = new User();
        mockUser.setMail("test@example.com");
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(authentication.isAuthenticated()).thenReturn(true);

        // Act
        String viewName = userFriendsController.showAddFriendPage(model);

        // Assert
        verify(model).addAttribute("userEmail", "test@example.com");
        assertEquals("addFriends", viewName);
    }

    @Test
    void testShowAddFriendPage_NonUserPrincipal() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn("anonymousUser");
        when(authentication.isAuthenticated()).thenReturn(true);

        // Act
        String viewName = userFriendsController.showAddFriendPage(model);

        // Assert
        verify(model).addAttribute("userEmail", "anonymous");
        assertEquals("addFriends", viewName);
    }

    @Test
    void testShowAddFriendPage_NotAuthenticated() {
        // Arrange
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act
        String viewName = userFriendsController.showAddFriendPage(model);

        // Assert
        verify(model).addAttribute("userEmail", "anonymous");
        assertEquals("addFriends", viewName);
    }

    @Test
    void testAddFriend_Success() {
        // Arrange
        User mockUser = new User();
        mockUser.setMail("test@example.com");
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(authentication.isAuthenticated()).thenReturn(true);
        String friendMail = "friend@example.com";

        // Act
        String viewName = userFriendsController.addFriend(friendMail, model);

        // Assert
        verify(userFriendsService).addFriend("test@example.com", friendMail);
        verify(model).addAttribute("message", "Ami ajouté avec succès !");
        assertEquals("addFriends", viewName);
    }

    @Test
    void testAddFriend_Exception() {
        // Arrange
        User mockUser = new User();
        mockUser.setMail("test@example.com");
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(authentication.isAuthenticated()).thenReturn(true);
        String friendMail = "friend@example.com";

        // Configure the service to throw an exception
        doThrow(new RuntimeException("L'ami n'existe pas")).when(userFriendsService).addFriend("test@example.com", friendMail);

        // Act
        String viewName = userFriendsController.addFriend(friendMail, model);

        // Assert
        verify(userFriendsService).addFriend("test@example.com", friendMail);
        verify(model).addAttribute("message", "Erreur : L'ami n'existe pas");
        assertEquals("addFriends", viewName);
    }

    @Test
    void testGetAuthenticatedUserEmail_UserPrincipal() {
        // Arrange
        User mockUser = new User();
        mockUser.setMail("test@example.com");
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(authentication.isAuthenticated()).thenReturn(true);

        // Act
        String result = userFriendsController.getAuthenticatedUserEmail();

        // Assert
        assertEquals("test@example.com", result);
    }

    @Test
    void testGetAuthenticatedUserEmail_NonUserPrincipal() {
        // Arrange
        when(authentication.getPrincipal()).thenReturn("anonymousUser");
        when(authentication.isAuthenticated()).thenReturn(true);

        // Act
        String result = userFriendsController.getAuthenticatedUserEmail();

        // Assert
        assertEquals("anonymous", result);
    }
}

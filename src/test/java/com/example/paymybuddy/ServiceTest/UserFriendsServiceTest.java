package com.example.paymybuddy.ServiceTest;

import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserRepository;
import com.example.paymybuddy.service.UserFriendsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserFriendsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserFriendsService userFriendsService;

    private User user;
    private User friend;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuration de l'utilisateur
        user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setMail("user@example.com");
        user.setFriends(new ArrayList<>());

        // Configuration d'un ami potentiel
        friend = new User();
        friend.setId(2L);
        friend.setUsername("friend");
        friend.setMail("friend@example.com");
        friend.setFriends(new ArrayList<>());

        // Configuration des mocks par défaut
        when(userRepository.findByMail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByMail("friend@example.com")).thenReturn(Optional.of(friend));
    }

    @Test
    void addFriend_Success_ShouldAddFriendToUser() {
        // Act
        userFriendsService.addFriend("user@example.com", "friend@example.com");

        // Assert
        assertTrue(user.getFriends().contains(friend));
        assertTrue(friend.getFriends().contains(user));
        verify(userRepository).save(user);
        verify(userRepository).save(friend);
    }

    @Test
    void addFriend_UserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByMail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userFriendsService.addFriend("nonexistent@example.com", "friend@example.com");
        });

        assertEquals("Utilisateur introuvable", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void addFriend_FriendNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByMail("friend@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userFriendsService.addFriend("user@example.com", "friend@example.com");
        });

        assertEquals("Ami introuvable", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void addFriend_SameUserAndFriend_ShouldThrowException() {
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userFriendsService.addFriend("user@example.com", "user@example.com");
        });

        assertEquals("Vous ne pouvez pas vous ajouter vous-même !", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void addFriend_AlreadyFriends_ShouldThrowException() {
        // Arrange
        // Ajouter l'ami à la liste des amis de l'utilisateur
        user.getFriends().add(friend);

        // Ajouter l'utilisateur à la liste des amis de l'ami
        friend.getFriends().add(user);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userFriendsService.addFriend("user@example.com", "friend@example.com");
        });

        assertEquals("Cet utilisateur est déjà votre ami", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
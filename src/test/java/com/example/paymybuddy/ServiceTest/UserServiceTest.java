package com.example.paymybuddy.ServiceTest;

import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserTransactionRepository;
import com.example.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserTransactionRepository userTransactionRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private List<User> testFriends;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Création de l'utilisateur de test
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setMail("test@example.com");
        testUser.setPassword("password");
        testUser.setBalance(500.0);

        // Création de la liste d'amis
        testFriends = new ArrayList<>();
        User friend1 = new User();
        friend1.setId(2L);
        friend1.setUsername("friend1");
        friend1.setMail("friend1@example.com");

        User friend2 = new User();
        friend2.setId(3L);
        friend2.setUsername("friend2");
        friend2.setMail("friend2@example.com");

        testFriends.add(friend1);
        testFriends.add(friend2);

        // Affectation de la liste d'amis à l'utilisateur
        testUser.setFriends(testFriends);

        // Configuration des mocks
        when(userTransactionRepository.findByMail("test@example.com")).thenReturn(Optional.of(testUser));
        when(userTransactionRepository.findByMail("nonexistent@example.com")).thenReturn(Optional.empty());
    }

    @Test
    void getUserByMail_ExistingUser_ShouldReturnUser() {
        // Act
        User result = userService.getUserByMail("test@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userTransactionRepository).findByMail("test@example.com");
    }

    @Test
    void getUserByMail_NonExistingUser_ShouldThrowException() {
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserByMail("nonexistent@example.com");
        });

        assertEquals("User not found", exception.getMessage());
        verify(userTransactionRepository).findByMail("nonexistent@example.com");
    }

    @Test
    void getUserFriends_ShouldReturnUserFriends() {
        // Act
        List<User> result = userService.getUserFriends(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(testFriends.size(), result.size());
        assertEquals(testFriends, result);
        // Pas de vérification sur userTransactionRepository car cette méthode n'interagit pas avec le repository
    }

    @Test
    void getUserFriends_UserWithNoFriends_ShouldReturnEmptyList() {
        // Arrange
        User userWithNoFriends = new User();
        userWithNoFriends.setId(4L);
        userWithNoFriends.setUsername("noFriends");
        userWithNoFriends.setMail("nofriends@example.com");
        userWithNoFriends.setFriends(new ArrayList<>());

        // Act
        List<User> result = userService.getUserFriends(userWithNoFriends);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
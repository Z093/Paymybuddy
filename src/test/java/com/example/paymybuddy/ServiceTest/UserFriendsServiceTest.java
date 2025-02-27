package com.example.paymybuddy.ServiceTest;

import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserFriendsRepository;
import com.example.paymybuddy.service.UserFriendsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserFriendsServiceTest {

    @Mock
    private UserFriendsRepository userFriendsRepository;

    @InjectMocks
    private UserFriendsService userFriendsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFriend_Success() {
        // Arrange
        String userMail = "user@example.com";
        String friendMail = "friend@example.com";

        User user = new User();
        user.setMail(userMail);
        user.setFriends(new HashSet<User>()); // Utilisation d'un HashSet pour un Set<User>

        User friend = new User();
        friend.setMail(friendMail);

        when(userFriendsRepository.findByMail(userMail)).thenReturn(Optional.of(user));
        when(userFriendsRepository.findByMail(friendMail)).thenReturn(Optional.of(friend));

        // Act
        userFriendsService.addFriend(userMail, friendMail);

        // Assert
        assertTrue(user.getFriends().contains(friend));

        verify(userFriendsRepository).save(user);
    }


    @Test
    void addFriend_UserNotFound() {
        // Arrange
        String userMail = "user@example.com";
        String friendMail = "friend@example.com";

        when(userFriendsRepository.findByMail(userMail)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userFriendsService.addFriend(userMail, friendMail)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userFriendsRepository, never()).save(any());
    }

    @Test
    void addFriend_FriendNotFound() {
        // Arrange
        String userMail = "user@example.com";
        String friendMail = "friend@example.com";

        User user = new User();
        user.setMail(userMail);
        user.setFriends(new HashSet<User>());

        when(userFriendsRepository.findByMail(userMail)).thenReturn(Optional.of(user));
        when(userFriendsRepository.findByMail(friendMail)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userFriendsService.addFriend(userMail, friendMail)
        );

        assertEquals("Friend not found", exception.getMessage());

        verify(userFriendsRepository, never()).save(any());
    }

    @Test
    void addFriend_AlreadyAFriend() {
        // Arrange
        String userMail = "user@example.com";
        String friendMail = "friend@example.com";

        User user = new User();
        user.setMail(userMail);
        user.setFriends(new HashSet<User>());// Correction : spécification du type

        User friend = new User();
        friend.setMail(friendMail);

        user.getFriends().add(friend);

        when(userFriendsRepository.findByMail(userMail)).thenReturn(Optional.of(user));
        when(userFriendsRepository.findByMail(friendMail)).thenReturn(Optional.of(friend));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userFriendsService.addFriend(userMail, friendMail)
        );

        assertEquals("This user is already a friend", exception.getMessage());

        verify(userFriendsRepository, never()).save(any());
    }
}

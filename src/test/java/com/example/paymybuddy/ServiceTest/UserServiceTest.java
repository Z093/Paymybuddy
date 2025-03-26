package com.example.paymybuddy.ServiceTest;

import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserTransactionRepository;
import com.example.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserTransactionRepository userTransactionRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setMail("test@example.com");
    }

    @Test
    void testGetUserByMail_UserExists() {
        when(userTransactionRepository.findByMail("test@example.com"))
                .thenReturn(Optional.of(user));

        User result = userService.getUserByMail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getMail());
        verify(userTransactionRepository, times(1)).findByMail("test@example.com");
    }

    @Test
    void testGetUserByMail_UserNotFound() {
        when(userTransactionRepository.findByMail("unknown@example.com"))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserByMail("unknown@example.com");
        });

        assertEquals("User not found", exception.getMessage());
        verify(userTransactionRepository, times(1)).findByMail("unknown@example.com");
    }

    @Test
    void testGetUserFriends() {
        User friend1 = new User();
        User friend2 = new User();
        user.setFriends(List.of(friend1, friend2));

        List<User> friends = userService.getUserFriends(user);

        assertNotNull(friends);
        assertEquals(2, friends.size());
    }
}
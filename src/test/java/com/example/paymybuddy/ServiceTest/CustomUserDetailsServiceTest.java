package com.example.paymybuddy.ServiceTest;

import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserRepository;
import com.example.paymybuddy.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserFound() {
        // Arrange
        String mail = "test@example.com";
        String password = "password";
        String role = "ROLE_USER";

        User user = new User();
        user.setMail(mail);
        user.setPassword(password);
        user.setRole(role);

        when(userRepository.findByMail(mail)).thenReturn(user);

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(mail);

        // Assert
        assertNotNull(userDetails);
        assertEquals(mail, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority(role)));

        verify(userRepository, times(1)).findByMail(mail);
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        // Arrange
        String mail = "unknown@example.com";

        when(userRepository.findByMail(mail)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername(mail)
        );

        assertEquals("User not found with Mail" + mail, exception.getMessage());
        verify(userRepository, times(1)).findByMail(mail);
    }
}


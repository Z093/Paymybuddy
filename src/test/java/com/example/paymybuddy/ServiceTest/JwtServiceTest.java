package com.example.paymybuddy.ServiceTest;

import com.example.paymybuddy.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private final String email = "test@example.com";
    private String token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();
        token = jwtService.generateToken(userDetails, email);
    }

    @Test
    void testGenerateToken() {
        assertNotNull(token);
        assertEquals(email, jwtService.extractUsername(token));
    }

    @Test
    void testExtractUsername() {
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals(email, extractedUsername);
    }

    @Test
    void testIsTokenValid() {
        assertTrue(jwtService.isTokenValid(token, userDetails, email));
    }

    @Test
    void testTokenShouldNotBeExpired() {
        assertTrue(jwtService.isTokenValid(token, userDetails, email)); // Teste indirectement l'expiration
    }
}

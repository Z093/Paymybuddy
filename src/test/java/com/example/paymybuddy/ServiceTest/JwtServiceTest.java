/*
package com.example.paymybuddy.ServiceTest;

import com.example.paymybuddy.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;
    private final String TEST_USERNAME = "test@example.com";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(TEST_USERNAME);
    }

    @Test
    void generateToken_ShouldReturnValidJwtToken() {
        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void generateTokenWithExtraClaims_ShouldIncludeThoseClaims() {
        // Arrange
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "USER");
        extraClaims.put("id", 123);

        // Act
        String token = jwtService.generateToken(extraClaims, userDetails);

        // Assert
        assertNotNull(token);
        assertEquals("USER", jwtService.extractClaim(token, claims -> claims.get("role")));
        assertEquals(123, ((Integer)jwtService.extractClaim(token, claims -> claims.get("id"))).intValue());
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        String extractedUsername = jwtService.extractUsername(token);

        // Assert
        assertEquals(TEST_USERNAME, extractedUsername);
    }

    @Test
    void isTokenValid_WithValidToken_ShouldReturnTrue() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_WithInvalidUsername_ShouldReturnFalse() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        UserDetails anotherUserDetails = mock(UserDetails.class);
        when(anotherUserDetails.getUsername()).thenReturn("another@example.com");

        // Act
        boolean isValid = jwtService.isTokenValid(token, anotherUserDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void extractClaim_ShouldReturnCorrectClaim() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);

        // Assert
        assertNotNull(issuedAt);
        assertTrue(issuedAt.before(new Date()) || issuedAt.equals(new Date()));
    }

    @Test
    void generateToken_TokenShouldNotBeExpired() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act & Assert - Testing private method through the public API
        boolean isExpired = jwtService.extractClaim(token, Claims::getExpiration).before(new Date());
        assertFalse(isExpired);
    }

    @Test
    void generateAndValidateToken_ShouldBeValid() {
        // Arrange & Act
        String token = jwtService.generateToken(userDetails);
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }
}*/

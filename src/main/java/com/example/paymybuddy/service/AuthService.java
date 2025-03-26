package com.example.paymybuddy.service;


import com.example.paymybuddy.dto.AuthReponse;
import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthReponse register(User user) {
        if (userRepository.existsByMail(user.getMail())) {
            throw new RuntimeException("Email already exists");
        }

        // Encoder le mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Sauvegarder l'utilisateur
        userRepository.save(user);

        // Générer le token
        String token = jwtService.generateToken(user, user.getMail());

        return new AuthReponse(token, "User registered successfully");
    }

    public AuthReponse loginUser(String mail, String password) {
        try {
            // Authentification
            log.info("Attempting to login user {}", mail);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(mail, password)
            );

            // Mettre à jour le SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            var user = userRepository.findByMail(mail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtService.generateToken(user,mail);
            log.debug("Generated token for user: {}", mail);

            return new AuthReponse(token, "Login successful");
        } catch (Exception e) {
            log.error("Login failed for user: {}", mail, e);
            throw new RuntimeException("Invalid credentials");
        }
    }
}
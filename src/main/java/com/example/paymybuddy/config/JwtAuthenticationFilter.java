package com.example.paymybuddy.config;

import com.example.paymybuddy.service.JwtService;
import com.example.paymybuddy.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        log.debug("Processing request: {}", request.getRequestURI());

        // Ne pas vérifier le token pour les URLs publiques
        if (request.getServletPath().contains("/api/auth") ||
                request.getServletPath().contains("/login") ||
                request.getServletPath().contains("/register") ||
                request.getServletPath().contains("/css")) {
            filterChain.doFilter(request, response);
            return;
        }


        // Vérifier le token dans la session d'abord
        HttpSession session = request.getSession(false);
        String token = session != null ? (String) session.getAttribute("token") : null;

        // Si pas de token dans la session, vérifier l'en-tête Authorization
        if (token == null) {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        log.debug("Token found: {}", token != null);

        if (token != null) {
            try {
                String mail = jwtService.extractUsername(token);
                String username = userService.getUserByMail(mail).getUsername();
                log.error("mail: {}", mail);
                log.debug("username: {}", username);
                if (mail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(mail);
                    log.debug("userDetails: {}", userDetails.getUsername());
                    if (jwtService.isTokenValid(token, userDetails, mail)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        log.debug("User authenticated: {}", mail);
                    }
                }
            } catch (Exception e) {
                log.error("Error processing JWT token", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
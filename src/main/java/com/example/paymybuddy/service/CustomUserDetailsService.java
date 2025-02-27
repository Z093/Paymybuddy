package com.example.paymybuddy.service;


import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


/*@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        System.out.println("🔍 Tentative de connexion avec : " + mail);
        User user = userRepository.findByMail(mail);

        if (user == null) {
            System.out.println("❌ Utilisateur non trouvé !");
            throw new UsernameNotFoundException("User not found with Mail: " + mail);
        }

        String role = user.getRole();
        if (role == null || role.isEmpty()) {
            role = "ROLE_USER"; // Définit un rôle par défaut
        }

        System.out.println("✅ Utilisateur trouvé : " + user.getMail() + " | Rôle : " + user.getRole());
        return new org.springframework.security.core.userdetails.User(
                user.getMail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }*/



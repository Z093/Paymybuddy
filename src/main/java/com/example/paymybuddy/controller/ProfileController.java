package com.example.paymybuddy.controller;

import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@RequestMapping("/api/v1")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/profile")
    public String showProfile(Model model) {
        // Récupération de l'utilisateur authentifié via SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mail;

        // Vérification du type d'authentification pour extraire l'email
        if (authentication.getPrincipal() instanceof User) {
            mail = ((User) authentication.getPrincipal()).getMail();
        } else {
            mail = authentication.getName();
        }

        System.out.println("Email/Username récupéré : " + mail);
        User user = userRepository.findByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateUser(@ModelAttribute User updatedUser, RedirectAttributes redirectAttributes) {
        // Récupération de l'utilisateur authentifié via SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mail;

        // Vérification du type d'authentification pour extraire l'email
        if (authentication.getPrincipal() instanceof User) {
            mail = ((User) authentication.getPrincipal()).getMail();
        } else {
            mail = authentication.getName();
        }

        return userRepository.findByMail(mail).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setMail(updatedUser.getMail());
            if (!updatedUser.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Encodage du mot de passe
            }
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("message", "Profil mis à jour avec succès !");
            return "redirect:/api/v1/profile";
        }).orElse("redirect:/api/v1/profile?error");
    }
}



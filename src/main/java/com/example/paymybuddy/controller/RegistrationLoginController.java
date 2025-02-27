package com.example.paymybuddy.controller;


import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/*@Controller
//@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RegistrationLoginController {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    // Afficher la page d'inscription
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
       return "register"; // Assurez-vous que "register" correspond au nom du fichier sans extension
    }

    // Gérer l'inscription
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userRepository.findByMail(user.getMail()) != null) {
            model.addAttribute("error", "L'email existe déjà !");
            return "register"; // Retourne à la page d'inscription avec un message d'erreur
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/api/v1/login"; // Redirection vers la page de connexion après inscription
    }


    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getMail(), user.getPassword())


            );

            if (authentication.isAuthenticated()) {
                return "redirect:/api/v1/transfer";
            } else {
                model.addAttribute("error", "Email ou mot de passe incorrect !");
                return "login";
            }
        } catch (AuthenticationException ex) {
            model.addAttribute("error", "Email ou mot de passe incorrect !");
            return "login";
        }
    }


    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile";
    }


}*/

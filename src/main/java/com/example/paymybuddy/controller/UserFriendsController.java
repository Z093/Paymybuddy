package com.example.paymybuddy.controller;

import com.example.paymybuddy.model.User;
import com.example.paymybuddy.service.UserFriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1")
public class UserFriendsController {

    @Autowired
    private UserFriendsService userFriendsService;

    @GetMapping("/add")
    public String showAddFriendPage(Model model) {
        String userEmail = getAuthenticatedUserEmail();
        model.addAttribute("userEmail", userEmail);
        return "addFriends";
    }

    public String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Vérifier le type d'objet principal
            Object principal = authentication.getPrincipal();
            if (principal instanceof User user) {
                return user.getMail();
            }
        }
        return "anonymous";
    }

    @PostMapping("/add")
    public String addFriend(@RequestParam String friendMail, Model model) {
        String userEmail = getAuthenticatedUserEmail();
        try {
            userFriendsService.addFriend(userEmail, friendMail);
            model.addAttribute("message", "Ami ajouté avec succès !");
        } catch (Exception e) {
            model.addAttribute("message", "Erreur : " + e.getMessage());
        }
        return "addFriends";
    }



}

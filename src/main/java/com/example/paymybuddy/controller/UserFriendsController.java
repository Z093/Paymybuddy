package com.example.paymybuddy.controller;

import com.example.paymybuddy.service.UserFriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


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

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername(); // Suppose que l'email est stocké comme username
        }
        return "anonymous";
    }


    @PostMapping("/add")
    public String addFriend(@RequestParam String userEmail, @RequestParam String friendMail, Model model) {
        try {
            userFriendsService.addFriend(userEmail, friendMail);
            model.addAttribute("message", "Ami ajouté avec succès !");
        } catch (Exception e) {
            model.addAttribute("message", "Erreur : " + e.getMessage());
        }
        return "addFriends";
    }



}

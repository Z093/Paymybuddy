package com.example.paymybuddy.controller;

import com.example.paymybuddy.service.UserFriendsService;
import org.springframework.beans.factory.annotation.Autowired;
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
        model.addAttribute("userEmail", "user@example.com"); // À remplacer dynamiquement
        return "addFriends"; // Le fichier Thymeleaf "add-friend.html"
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

package com.example.paymybuddy.controller;


import com.example.paymybuddy.dto.AuthResponse;
import com.example.paymybuddy.model.User;
import com.example.paymybuddy.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            var newUser = new User();
            newUser.setUsername(user.getUsername());
            newUser.setMail(user.getMail());
            newUser.setPassword(user.getPassword());
            newUser.setRole("USER");
            newUser.setBalance(100.0);

            //AuthResponse response = authService.register(newUser);
            return "redirect:/api/v1/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String mail,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        try {
            AuthResponse response = authService.loginUser(mail, password);
            session.setAttribute("token", response.getToken());
            return "redirect:/api/v1/transfer";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

}
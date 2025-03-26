package com.example.paymybuddy.controller;

import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.model.User;
import com.example.paymybuddy.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/transfer")
    public String showTransferPage(@AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            log.error("User is null in showTransferPage");
            return "redirect:/api/v1/login";
        }

        log.info("User accessing transfer page: {}", user.getMail());

        model.addAttribute("friends", user.getFriends());
        model.addAttribute("balance", user.getBalance());
        List<Transaction> transactions = transactionService.getUserTransactions(user);
        model.addAttribute("transactions", transactions);
        return "transfer";
    }

    @PostMapping("/transfer")
    @ResponseBody
    public String makePayment(
            @AuthenticationPrincipal User sender,
            @RequestParam Long receiverId,
            @RequestParam double amount,
            @RequestParam String description) {
        try {
            if (sender == null) {
                return "User not authenticated";
            }
            transactionService.makePayment(sender.getId(), receiverId, amount, description);
            return "transfer";
        } catch (Exception e) {
            log.error("Transfer failed", e);
            return "Transfer failed: " + e.getMessage();
        }
    }
}
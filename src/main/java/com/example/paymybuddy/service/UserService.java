package com.example.paymybuddy.service;

import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.UserTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserTransactionRepository userTransactionRepository;

    public User getUserByMail(String mail) {
        return userTransactionRepository.findByMail(mail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getUserFriends(User user) {
        return (List<User>) user.getFriends();
    }
}

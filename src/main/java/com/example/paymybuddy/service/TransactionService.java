package com.example.paymybuddy.service;

import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.TransactionRepository;
import com.example.paymybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public void makePayment(Long senderId, Long receiverId, double amount, String description) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Vérifiez si l'expéditeur a suffisamment de solde
        if (sender.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        // Mettez à jour les soldes
        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        // Enregistrez la transaction avec description
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);

        // Sauvegardez les modifications des utilisateurs
        userRepository.save(sender);
        userRepository.save(receiver);
    }

    public List<Transaction> getUserTransactions(User user) {
        return transactionRepository.findBySenderOrReceiverOrderByTimestampDesc(user, user);
    }
}


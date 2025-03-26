package com.example.paymybuddy.ServiceTest;

import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.model.User;
import com.example.paymybuddy.repository.TransactionRepository;
import com.example.paymybuddy.repository.UserRepository;
import com.example.paymybuddy.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private User sender;
    private User receiver;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuration des utilisateurs de test
        sender = new User();
        sender.setId(1L);
        sender.setUsername("sender");
        sender.setMail("sender@example.com");
        sender.setBalance(500.0);

        receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("receiver");
        receiver.setMail("receiver@example.com");
        receiver.setBalance(100.0);

        // Configuration d'une transaction de test
        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(100.0);
        transaction.setDescription("Test transaction");
        transaction.setTimestamp(LocalDateTime.now());

        // Configuration des mocks par défaut
        when(userRepository.findById(1L)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));
    }

    @Test
    void makePayment_Success_ShouldUpdateBalancesAndSaveTransaction() {
        // Arrange
        double amount = 100.0;
        String description = "Test payment";
        double expectedSenderBalance = sender.getBalance() - amount;
        double expectedReceiverBalance = receiver.getBalance() + amount;

        // Act
        transactionService.makePayment(1L, 2L, amount, description);

        // Assert
        // Vérifier que les utilisateurs ont été mis à jour avec les bons soldes
        assertEquals(expectedSenderBalance, sender.getBalance());
        assertEquals(expectedReceiverBalance, receiver.getBalance());

        // Vérifier que les utilisateurs ont été sauvegardés
        verify(userRepository).save(sender);
        verify(userRepository).save(receiver);

        // Vérifier que la transaction a été sauvegardée avec les bonnes valeurs
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());

        Transaction savedTransaction = transactionCaptor.getValue();
        assertEquals(sender, savedTransaction.getSender());
        assertEquals(receiver, savedTransaction.getReceiver());
        assertEquals(amount, savedTransaction.getAmount());
        assertEquals(description, savedTransaction.getDescription());
        assertNotNull(savedTransaction.getTimestamp());
    }

    @Test
    void makePayment_SenderNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.makePayment(1L, 2L, 100.0, "Test payment");
        });

        assertEquals("Sender not found", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void makePayment_ReceiverNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.makePayment(1L, 2L, 100.0, "Test payment");
        });

        assertEquals("Receiver not found", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void makePayment_InsufficientBalance_ShouldThrowException() {
        // Arrange
        double amount = 600.0; // Plus que le solde de l'expéditeur

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.makePayment(1L, 2L, amount, "Test payment");
        });

        assertEquals("Insufficient balance", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserTransactions_ShouldReturnUserTransactions() {
        // Arrange
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        when(transactionRepository.findBySenderOrReceiverOrderByTimestampDesc(sender, sender))
                .thenReturn(transactions);

        // Act
        List<Transaction> result = transactionService.getUserTransactions(sender);

        // Assert
        assertEquals(transactions, result);
        verify(transactionRepository).findBySenderOrReceiverOrderByTimestampDesc(sender, sender);
    }
}

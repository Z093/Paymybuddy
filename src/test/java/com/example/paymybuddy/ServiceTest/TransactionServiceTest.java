/*
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void makePayment_SuccessfulTransaction() {
        // Arrange
        Long senderId = 1L;
        Long receiverId = 2L;
        double amount = 50.0;

        User sender = new User();
        sender.setId(senderId);
        sender.setBalance(100.0);

        User receiver = new User();
        receiver.setId(receiverId);
        receiver.setBalance(20.0);

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

        // Act
        transactionService.makePayment(senderId, receiverId, amount);

        // Assert
        assertEquals(50.0, sender.getBalance());
        assertEquals(70.0, receiver.getBalance());

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());

        Transaction capturedTransaction = transactionCaptor.getValue();
        assertEquals(sender, capturedTransaction.getSender());
        assertEquals(receiver, capturedTransaction.getReceiver());
        assertEquals(amount, capturedTransaction.getAmount());
        assertNotNull(capturedTransaction.getTimestamp());

        verify(userRepository).save(sender);
        verify(userRepository).save(receiver);
    }

    @Test
    void makePayment_InsufficientBalance() {
        // Arrange
        Long senderId = 1L;
        Long receiverId = 2L;
        double amount = 150.0;

        User sender = new User();
        sender.setId(senderId);
        sender.setBalance(100.0);

        User receiver = new User();
        receiver.setId(receiverId);
        receiver.setBalance(20.0);

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                transactionService.makePayment(senderId, receiverId, amount)
        );

        assertEquals("Insufficient balance", exception.getMessage());

        verify(transactionRepository, never()).save(any());
        verify(userRepository, never()).save(sender);
        verify(userRepository, never()).save(receiver);
    }

    @Test
    void makePayment_SenderNotFound() {
        // Arrange
        Long senderId = 1L;
        Long receiverId = 2L;

        when(userRepository.findById(senderId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                transactionService.makePayment(senderId, receiverId, 50.0)
        );

        assertEquals("Sender not found", exception.getMessage());

        verify(transactionRepository, never()).save(any());
    }

    @Test
    void makePayment_ReceiverNotFound() {
        // Arrange
        Long senderId = 1L;
        Long receiverId = 2L;

        User sender = new User();
        sender.setId(senderId);
        sender.setBalance(100.0);

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                transactionService.makePayment(senderId, receiverId, 50.0)
        );

        assertEquals("Receiver not found", exception.getMessage());

        verify(transactionRepository, never()).save(any());
        verify(userRepository, never()).save(sender);
    }
}

*/

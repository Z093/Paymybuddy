package com.example.paymybuddy.ControllerTest;

import com.example.paymybuddy.controller.TransactionController;
import com.example.paymybuddy.model.Transaction;
import com.example.paymybuddy.model.User;
import com.example.paymybuddy.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private Model model;

    @Mock
    private User testUser;

    @InjectMocks
    private TransactionController transactionController;

    private List<Transaction> testTransactions;
    private List<User> testFriendsList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configuration de l'utilisateur mockito
        when(testUser.getId()).thenReturn(1L);
        when(testUser.getUsername()).thenReturn("testUser");
        when(testUser.getMail()).thenReturn("test@example.com");
        when(testUser.getPassword()).thenReturn("password");
        when(testUser.getBalance()).thenReturn(500.0);

        // Configuration des amis de test
        testFriendsList = new ArrayList<>();
        User friend1 = new User();
        friend1.setId(2L);
        friend1.setUsername("friend1");
        friend1.setMail("friend1@example.com");

        User friend2 = new User();
        friend2.setId(3L);
        friend2.setUsername("friend2");
        friend2.setMail("friend2@example.com");

        testFriendsList.add(friend1);
        testFriendsList.add(friend2);

        // Mock la méthode getFriends pour retourner une List
        when(testUser.getFriends()).thenReturn(testFriendsList);

        // Configuration des transactions de test
        testTransactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setSender(testUser);
        transaction1.setReceiver(friend1);
        transaction1.setAmount(100.0);
        transaction1.setDescription("Test transaction 1");

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setSender(testUser);
        transaction2.setReceiver(friend2);
        transaction2.setAmount(50.0);
        transaction2.setDescription("Test transaction 2");

        testTransactions.add(transaction1);
        testTransactions.add(transaction2);
    }

    @Test
    void showTransferPage_AuthenticatedUser_ShouldReturnTransferView() {
        // Arrange
        when(transactionService.getUserTransactions(testUser)).thenReturn(testTransactions);

        // Act
        String viewName = transactionController.showTransferPage(testUser, model);

        // Assert
        assertEquals("transfer", viewName);
        verify(model).addAttribute("friends", testFriendsList);
        verify(model).addAttribute("balance", 500.0);
        verify(model).addAttribute("transactions", testTransactions);
        verify(transactionService).getUserTransactions(testUser);
    }

    @Test
    void showTransferPage_UnauthenticatedUser_ShouldRedirectToLogin() {
        // Act
        String viewName = transactionController.showTransferPage(null, model);

        // Assert
        assertEquals("redirect:/api/v1/login", viewName);
        verifyNoInteractions(model);
        verifyNoInteractions(transactionService);
    }

    @Test
    void makePayment_Success_ShouldRedirectToTransfer() {
        // Arrange
        Long receiverId = 2L;
        double amount = 50.0;
        String description = "Test payment";

        // Act
        String result = transactionController.makePayment(testUser, receiverId, amount, description);

        // Assert
        assertEquals("redirect:/api/v1/transfer", result);
        verify(transactionService).makePayment(1L, receiverId, amount, description);
    }

    @Test
    void makePayment_ServiceException_ShouldReturnErrorMessage() {
        // Arrange
        Long receiverId = 2L;
        double amount = 50.0;
        String description = "Test payment";
        String errorMessage = "Insufficient funds";

        doThrow(new RuntimeException(errorMessage))
                .when(transactionService).makePayment(anyLong(), anyLong(), anyDouble(), anyString());

        // Act
        String result = transactionController.makePayment(testUser, receiverId, amount, description);

        // Assert
        assertEquals("Transfer failed: " + errorMessage, result);
        verify(transactionService).makePayment(1L, receiverId, amount, description);
    }

    @Test
    void makePayment_UnauthenticatedUser_ShouldReturnErrorMessage() {
        // Arrange
        Long receiverId = 2L;
        double amount = 50.0;
        String description = "Test payment";

        // Act
        String result = transactionController.makePayment(null, receiverId, amount, description);

        // Assert
        assertEquals("User not authenticated", result);
        verifyNoInteractions(transactionService);
    }
}
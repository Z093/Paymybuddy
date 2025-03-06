package com.example.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "transaction")
@Data
public class Transaction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender; // L'utilisateur qui envoie de l'argent

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver; // L'utilisateur qui reçoit l'argent

    private double amount; // Montant du transfert

    private String description;

    private LocalDateTime timestamp; // Date et heure de la transaction
}







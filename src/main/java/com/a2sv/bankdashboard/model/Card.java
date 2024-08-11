package com.a2sv.bankdashboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double balance;
    private String cardHolder;
    private Date expiryDate;
    private String cardNumber;
    private String passcode;
    private String cardType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
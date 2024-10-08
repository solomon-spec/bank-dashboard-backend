package com.a2sv.bankdashboard.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Document(collection = "cards")
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    private String id;

    private double balance;
    private String cardHolder;
    private Date expiryDate;
    private String cardNumber;
    private String passcode;
    private String cardType;

    @DBRef
    private User user;
}
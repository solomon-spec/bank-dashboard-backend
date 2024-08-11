package com.a2sv.bankdashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResponseDetailed {

    private String id;

    private double balance;

    private String cardHolder;

    private Date expiryDate;

    private String cardNumber;

    private String passcode;

    private String cardType;


    private Integer userId;
}
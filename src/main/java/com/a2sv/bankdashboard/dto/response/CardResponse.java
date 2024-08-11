package com.a2sv.bankdashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResponse {
    private Long id;
    private String cardHolder;
    private String semiCardNumber;
    private String cardType;
    private Date ExpiryDate;
}
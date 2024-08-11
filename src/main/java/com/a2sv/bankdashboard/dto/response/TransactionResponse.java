package com.a2sv.bankdashboard.dto.response;

import com.a2sv.bankdashboard.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private Long transactionId;
    private TransactionType type;
    private String senderUserName;
    private String description;
    private LocalDate date;
    private double amount;
    private String receiverUserName;
}
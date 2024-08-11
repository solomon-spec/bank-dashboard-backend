package com.a2sv.bankdashboard.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "activeLoans")
@AllArgsConstructor
@NoArgsConstructor
public class ActiveLoan {
    @Id
    private String serialNumber;
    private double loanAmount;
    private double amountLeftToRepay;
    private int duration;
    private double interestRate;
    private double installment;
    private ActiveLoanType type;
    private ActiveLoneStatus activeLoneStatus;

    @DBRef
    private User user;
}
package com.a2sv.bankdashboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ActiveLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialNumber;
    private double loanAmount;
    private double amountLeftToRepay;
    private int duration;
    private double interestRate;
    private double installment;
    private ActiveLoanType type;
    private ActiveLoneStatus activeLoneStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
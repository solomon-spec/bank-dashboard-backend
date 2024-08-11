package com.a2sv.bankdashboard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ActiveLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serialNumber;
    private double loanAmount;
    private double amountLeftToRepay;
    private int duration;
    private double interestRate;
    private double installment;
    private String type; // Personal, Corporate, Business
}
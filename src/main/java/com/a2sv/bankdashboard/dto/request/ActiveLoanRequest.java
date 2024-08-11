package com.a2sv.bankdashboard.dto.request;

import com.a2sv.bankdashboard.model.ActiveLoanType;
import lombok.Data;

@Data
public class ActiveLoanRequest {
    private double loanAmount;
    private int duration;
    private double interestRate;
    private ActiveLoanType type;
}
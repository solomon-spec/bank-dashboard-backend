package com.a2sv.bankdashboard.dto.response;

import com.a2sv.bankdashboard.model.ActiveLoanType;
import com.a2sv.bankdashboard.model.ActiveLoneStatus;
import lombok.Data;

@Data
public class ActiveLoanResponse {
    private Long serialNumber;
    private double loanAmount;
    private double amountLeftToRepay;
    private int duration;
    private double interestRate;
    private double installment;
    private ActiveLoanType type;
    private ActiveLoneStatus activeLoneStatus;
    private Long userId;
}
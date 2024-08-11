package com.a2sv.bankdashboard.dto.request;

import com.a2sv.bankdashboard.model.ActiveLoanType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActiveLoanRequest {
    @DecimalMin(value = "0.0", inclusive = false, message = "Loan amount must be greater than zero")
    private double loanAmount;

    @Min(value = 1, message = "Duration must be at least 1 month")
    private int duration;

    @DecimalMin(value = "0.0", inclusive = false, message = "Interest rate must be greater than zero")
    private double interestRate;

    @NotNull(message = "Loan type must not be null")
    private ActiveLoanType type;
}
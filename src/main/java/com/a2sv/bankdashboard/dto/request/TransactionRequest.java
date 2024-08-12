package com.a2sv.bankdashboard.dto.request;

import com.a2sv.bankdashboard.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidateReceiverUsername
public class TransactionRequest {

    @NotNull(message = "Type is mandatory")
    private TransactionType type;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Date is mandatory")
    private LocalDate date;

    @NotNull(message = "Amount is mandatory")
    private double amount;
    private String receiverUserName;
}
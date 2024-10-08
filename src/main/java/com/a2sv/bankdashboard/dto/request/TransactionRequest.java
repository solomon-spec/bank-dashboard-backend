package com.a2sv.bankdashboard.dto.request;

import com.a2sv.bankdashboard.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidateReceiverUsername
public class TransactionRequest {

    @NotNull(message = "Type is mandatory")
    private TransactionType type;

    @NotBlank(message = "Description is mandatory")
    private String description;


    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount can not be less than or equal to zero")
    private double amount;
    private String receiverUserName;
}
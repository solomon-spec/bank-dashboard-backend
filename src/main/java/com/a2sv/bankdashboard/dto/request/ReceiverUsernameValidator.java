package com.a2sv.bankdashboard.dto.request;

import com.a2sv.bankdashboard.model.TransactionType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReceiverUsernameValidator implements ConstraintValidator<ValidateReceiverUsername, TransactionRequest> {

    @Override
    public boolean isValid(TransactionRequest transactionRequest, ConstraintValidatorContext context) {
        if (transactionRequest.getType() == TransactionType.transfer) {
            return transactionRequest.getReceiverUserName() != null && !transactionRequest.getReceiverUserName().isEmpty();
        }
        return true;
    }
}
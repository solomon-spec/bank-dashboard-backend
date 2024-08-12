package com.a2sv.bankdashboard.dto.request;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ReceiverUsernameValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateReceiverUsername {
    String message() default "Receiver username is required for transfer transactions";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
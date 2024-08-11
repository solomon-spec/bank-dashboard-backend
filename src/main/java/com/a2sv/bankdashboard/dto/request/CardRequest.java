package com.a2sv.bankdashboard.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class CardRequest {
    @Min(0)
    private double balance;

    @NotBlank
    private String cardHolder;

    @NotNull
    @Future
    private Date expiryDate;

    @NotBlank
    @Pattern(regexp = "\\d{4,6}", message = "Passcode must be 4 to 6 digits")
    private String passcode;

    @NotBlank
    private String cardType;
}
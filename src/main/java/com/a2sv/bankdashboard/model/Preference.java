package com.a2sv.bankdashboard.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Embeddable
public class Preference {
    @NotBlank(message = "Currency is mandatory")
    private String currency;

    @NotNull(message = "Sent or Receive Digital Currency preference is mandatory")
    private boolean sentOrReceiveDigitalCurrency;

    @NotNull(message = "Receive Merchant Order preference is mandatory")
    private boolean receiveMerchantOrder;

    @NotNull(message = "Account Recommendations preference is mandatory")
    private boolean accountRecommendations;

    @NotBlank(message = "Time Zone is mandatory")
    private String timeZone;

    @NotNull(message = "Two Factor Authentication preference is mandatory")
    private boolean twoFactorAuthentication;
}
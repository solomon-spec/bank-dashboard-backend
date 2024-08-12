package com.a2sv.bankdashboard.model;

import org.springframework.data.mongodb.core.mapping.Field;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Preference {
    @Field
    @NotBlank(message = "Currency is mandatory")
    private String currency;

    @Field
    @NotNull(message = "Sent or Receive Digital Currency preference is mandatory")
    private Boolean sentOrReceiveDigitalCurrency;

    @Field
    @NotNull(message = "Receive Merchant Order preference is mandatory")
    private Boolean receiveMerchantOrder;

    @Field
    @NotNull(message = "Account Recommendations preference is mandatory")
    private Boolean accountRecommendations;

    @Field
    @NotBlank(message = "Time Zone is mandatory")
    private String timeZone;

    @Field
    @NotNull(message = "Two Factor Authentication preference is mandatory")
    private Boolean twoFactorAuthentication;
}
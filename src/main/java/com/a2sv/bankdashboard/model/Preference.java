package com.a2sv.bankdashboard.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Preference {
    private String currency;
    private boolean sentOrReceiveDigitalCurrency;
    private boolean receiveMerchantOrder;
    private boolean accountRecommendations;
    private String timeZone;
    private boolean twoFactorAuthentication;
}
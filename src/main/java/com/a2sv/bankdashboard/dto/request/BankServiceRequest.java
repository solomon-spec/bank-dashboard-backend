package com.a2sv.bankdashboard.dto.request;

import lombok.Data;

@Data
public class BankServiceRequest {
    private String name;
    private String details;
    private int numberOfUsers;
    private String status;
    private String type;
    private String icon;
}
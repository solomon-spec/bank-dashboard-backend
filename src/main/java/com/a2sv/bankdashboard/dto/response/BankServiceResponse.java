package com.a2sv.bankdashboard.dto.response;

import lombok.Data;

@Data
public class BankServiceResponse {
    private Long id;
    private String name;
    private String details;
    private int numberOfUsers;
    private String status;
    private String type;
    private String icon;
}
package com.a2sv.bankdashboard.dto.response;

import lombok.Data;

@Data
public class CompanyResponse {
    private String id;
    private String companyName;
    private String type;
    private String icon;
}
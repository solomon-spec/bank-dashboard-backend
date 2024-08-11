package com.a2sv.bankdashboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CompanyRequest {
    @NotBlank(message = "Company name is mandatory")
    private String companyName;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @NotBlank(message = "Icon is mandatory")
    private String icon;
}
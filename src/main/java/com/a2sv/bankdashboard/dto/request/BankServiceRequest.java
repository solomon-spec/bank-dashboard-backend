package com.a2sv.bankdashboard.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BankServiceRequest {
    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Details must not be blank")
    private String details;

    @Min(value = 0, message = "Number of users must be at least 0")
    private int numberOfUsers;

    @NotBlank(message = "Status must not be blank")
    private String status;

    @NotBlank(message = "Type must not be blank")
    private String type;

    @NotBlank(message = "Icon must not be blank")
    private String icon;
}
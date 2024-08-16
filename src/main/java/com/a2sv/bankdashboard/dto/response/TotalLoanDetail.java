package com.a2sv.bankdashboard.dto.response;

import lombok.Data;

@Data
public class TotalLoanDetail {
    private Double personalLoan;
    private Double businessLoan;
    private Double corporateLoan;
}

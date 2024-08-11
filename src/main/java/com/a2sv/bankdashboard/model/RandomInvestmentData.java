package com.a2sv.bankdashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RandomInvestmentData {
    private double totalInvestment;
    private double rateOfReturn;
    private List<TimeValue> yearlyTotalInvestment;
    private List<TimeValue> monthlyRevenue;
}

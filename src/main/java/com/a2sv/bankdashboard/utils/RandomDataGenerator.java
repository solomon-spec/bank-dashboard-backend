package com.a2sv.bankdashboard.utils;

import com.a2sv.bankdashboard.model.RandomInvestmentData;
import com.a2sv.bankdashboard.model.TimeValue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomDataGenerator {
    private final Random random = new Random();
    public RandomInvestmentData generateRandomInvestmentData(int years, int months) {
        double totalInvestment = 10000 + (50000 - 10000) * random.nextDouble();
        double rateOfReturn = 1 + (15 - 1) * random.nextDouble();
        int currentYear = LocalDate.now().getYear();

        List<TimeValue> yearlyTotalInvestment = IntStream.range(0, years)
                .mapToObj(i -> new TimeValue(String.valueOf(currentYear - i), 5000 + (20000 - 5000) * random.nextDouble()))
                .collect(Collectors.toList());

        List<TimeValue> monthlyRevenue = IntStream.range(0, months)
                .mapToObj(i -> {
                    LocalDate date = LocalDate.now().minusMonths(i);
                    return new TimeValue(date.format(DateTimeFormatter.ofPattern("MM/yyyy")), 1000 + (5000 - 1000) * random.nextDouble());
                })
                .collect(Collectors.toList());

        return new RandomInvestmentData(totalInvestment, rateOfReturn, yearlyTotalInvestment, monthlyRevenue);
    }
}

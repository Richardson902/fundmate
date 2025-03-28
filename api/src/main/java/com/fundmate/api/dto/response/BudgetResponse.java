package com.fundmate.api.dto.response;

import com.fundmate.api.model.BudgetDuration;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BudgetResponse {
    private Long id;
    private Double amount;
    private CategoryResponse category;
    private AccountResponse account;
    private BudgetDuration duration;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double spentAmount;
    private Double residualAmount;
    private Double completionPercentage;
}

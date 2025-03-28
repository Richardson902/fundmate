package com.fundmate.api.dto.request;

import com.fundmate.api.model.BudgetDuration;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BudgetRequest {
    private Double amount;
    private Long categoryId;
    private Long accountId;
    private BudgetDuration duration;
    private LocalDate startDate;
    private LocalDate endDate;
}

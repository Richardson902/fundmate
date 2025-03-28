package com.fundmate.api.service;

import com.fundmate.api.dto.request.BudgetRequest;
import com.fundmate.api.dto.response.BudgetResponse;
import com.fundmate.api.model.Budget;

import java.util.List;

public interface BudgetService {
    BudgetResponse createBudget(BudgetRequest budgetRequest);
    List<BudgetResponse> getBudgetsByAccountId(Long accountId);
    BudgetResponse getBudgetById(Long id);
    void deleteBudget(Long id);
}

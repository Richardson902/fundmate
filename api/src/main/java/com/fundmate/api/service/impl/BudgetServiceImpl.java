package com.fundmate.api.service.impl;

import com.fundmate.api.dto.request.BudgetRequest;
import com.fundmate.api.dto.response.BudgetResponse;
import com.fundmate.api.mapper.BudgetMapper;
import com.fundmate.api.model.Account;
import com.fundmate.api.model.Budget;
import com.fundmate.api.model.Category;
import com.fundmate.api.repository.AccountRepository;
import com.fundmate.api.repository.BudgetRepository;
import com.fundmate.api.repository.CategoryRepository;
import com.fundmate.api.repository.TransactionRepository;
import com.fundmate.api.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetMapper budgetMapper;

    public BudgetServiceImpl(BudgetRepository budgetRepository, TransactionRepository transactionRepository, AccountRepository accountRepository, CategoryRepository categoryRepository, BudgetMapper budgetMapper) {
        this.budgetRepository = budgetRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.budgetMapper = budgetMapper;
    }

    @Override
    public BudgetResponse createBudget(BudgetRequest budgetRequest) {
        Budget budget = budgetMapper.toEntity(budgetRequest);

        Account account = accountRepository.findById(budgetRequest.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        Category category = categoryRepository.findById(budgetRequest.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        budget.setAccount(account);
        budget.setCategory(category);

        Budget savedBudget = budgetRepository.save(budget);
        return budgetMapper.toResponse(savedBudget);
    }

    @Override
    public List<BudgetResponse> getBudgetsByAccountId(Long accountId) {
        return budgetRepository.findByAccountId(accountId).stream()
                .map(this::enrichBudgetResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BudgetResponse getBudgetById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found"));
        return enrichBudgetResponse(budget);
    }

    @Override
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

    private BudgetResponse enrichBudgetResponse(Budget budget) {
        BudgetResponse response = budgetMapper.toResponse(budget);

        // Get total spent amount using repository query
        Double spentAmount = transactionRepository.sumAmountByAccountIdAndCategoryIdAndDateBetween(
                budget.getAccount().getId(),
                budget.getCategory().getId(),
                budget.getStartDate(),
                budget.getEndDate());

        // Handle null result from query
        spentAmount = spentAmount == null ? 0.0 : spentAmount;

        // Calculate remaining amount and completion percentage
        Double residualAmount = budget.getAmount() - spentAmount;
        Double completionPercentage = (budget.getAmount() > 0) ?
                (spentAmount / budget.getAmount()) * 100 : 0.0;

        // Enrich the response with calculated values
        response.setSpentAmount(spentAmount);
        response.setResidualAmount(residualAmount);
        response.setCompletionPercentage(completionPercentage);

        return response;
    }


}

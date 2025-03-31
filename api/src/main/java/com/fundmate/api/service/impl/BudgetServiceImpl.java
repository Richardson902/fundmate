package com.fundmate.api.service.impl;

import com.fundmate.api.dto.request.BudgetRequest;
import com.fundmate.api.dto.response.BudgetResponse;
import com.fundmate.api.mapper.BudgetMapper;
import com.fundmate.api.model.Account;
import com.fundmate.api.model.Budget;
import com.fundmate.api.model.Category;
import com.fundmate.api.model.User;
import com.fundmate.api.repository.AccountRepository;
import com.fundmate.api.repository.BudgetRepository;
import com.fundmate.api.repository.CategoryRepository;
import com.fundmate.api.repository.TransactionRepository;
import com.fundmate.api.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
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

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public BudgetResponse createBudget(BudgetRequest budgetRequest) {
        Budget budget = budgetMapper.toEntity(budgetRequest);

        Account account = accountRepository.findById(budgetRequest.getAccountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        Category category = categoryRepository.findById(budgetRequest.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if (!account.getUser().getId().equals(getCurrentUser().getId()) || !category.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        budget.setAccount(account);
        budget.setCategory(category);
        setDatesByDuration(budget, budgetRequest);

        Budget savedBudget = budgetRepository.save(budget);
        return budgetMapper.toResponse(savedBudget);
    }

    private void setDatesByDuration(Budget budget, BudgetRequest request) {
        LocalDate startDate = LocalDate.now();
        budget.setStartDate(startDate);

        LocalDate endDate = switch (request.getDuration()) {
            case ONE_WEEK -> startDate.plusWeeks(1);
            case ONE_MONTH -> startDate.plusMonths(1);
            case ONE_YEAR -> startDate.plusYears(1);
        };

        budget.setEndDate(endDate);
    }

    @Override
    public List<BudgetResponse> getBudgetsByAccountId(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if (!account.getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return budgetRepository.findByAccountId(accountId).stream()
                .map(this::enrichBudgetResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BudgetResponse getBudgetById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found"));

        if (!budget.getAccount().getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return enrichBudgetResponse(budget);
    }

    @Override
    public void deleteBudget(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget not found"));

        if (!budget.getAccount().getUser().getId().equals(getCurrentUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

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

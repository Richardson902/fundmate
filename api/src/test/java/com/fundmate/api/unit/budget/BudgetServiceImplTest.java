package com.fundmate.api.unit.budget;

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
import com.fundmate.api.service.impl.BudgetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceImplTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BudgetMapper budgetMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    private BudgetRequest budgetRequest;
    private Budget budget;
    private BudgetResponse budgetResponse;
    private Account account;
    private Category category;
    private User user;

    private void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        account = new Account();
        account.setId(1L);
        account.setUser(user);

        category = new Category();
        category.setId(1L);
        category.setUser(user);

        budgetRequest = new BudgetRequest();
        budgetRequest.setAmount(1000.0);
        budgetRequest.setCategoryId(1L);
        budgetRequest.setAccountId(1L);
        budgetRequest.setStartDate(LocalDate.now());
        budgetRequest.setEndDate(LocalDate.now().plusMonths(1));

        budget = new Budget();
        budget.setId(1L);
        budget.setAmount(1000.0);
        budget.setAccount(account);
        budget.setCategory(category);
        budget.setStartDate(LocalDate.now());
        budget.setEndDate(LocalDate.now().plusMonths(1));

        budgetResponse = new BudgetResponse();
        budgetResponse.setId(1L);
        budgetResponse.setAmount(1000.0);
        budgetResponse.setSpentAmount(200.0);
        budgetResponse.setResidualAmount(800.0);
        budgetResponse.setCompletionPercentage(20.0);
    }

    @Test
    void createBudget_ShouldCreateAndReturnBudget() {
        setupSecurityContext();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(budgetMapper.toEntity(budgetRequest)).thenReturn(budget);
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);
        when(budgetMapper.toResponse(budget)).thenReturn(budgetResponse);

        BudgetResponse result = budgetService.createBudget(budgetRequest);

        assertNotNull(result);
        assertEquals(budgetResponse.getId(), result.getId());
        assertEquals(budgetResponse.getAmount(), result.getAmount());
        verify(budgetRepository).save(budget);
    }

    @Test
    void getBudgetsByAccountId_ShouldReturnList() {
        setupSecurityContext();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(budgetRepository.findByAccountId(1L)).thenReturn(Arrays.asList(budget));
        when(budgetMapper.toResponse(budget)).thenReturn(budgetResponse);
        when(transactionRepository.sumAmountByAccountIdAndCategoryIdAndDateBetween(
                any(), any(), any(), any())).thenReturn(200.0);

        List<BudgetResponse> result = budgetService.getBudgetsByAccountId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(budgetResponse.getSpentAmount(), result.get(0).getSpentAmount());
        verify(budgetRepository).findByAccountId(1L);
    }

    @Test
    void getBudgetById_ShouldReturnBudget() {
        setupSecurityContext();
        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));
        when(budgetMapper.toResponse(budget)).thenReturn(budgetResponse);
        when(transactionRepository.sumAmountByAccountIdAndCategoryIdAndDateBetween(
                any(), any(), any(), any())).thenReturn(200.0);

        BudgetResponse result = budgetService.getBudgetById(1L);

        assertNotNull(result);
        assertEquals(budgetResponse.getId(), result.getId());
        assertEquals(budgetResponse.getSpentAmount(), result.getSpentAmount());
        verify(budgetRepository).findById(1L);
    }

    @Test
    void getBudgetById_WithInvalidId_ShouldThrowException() {
        when(budgetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () ->
                budgetService.getBudgetById(1L));
    }

    @Test
    void deleteBudget_ShouldDelete() {
        setupSecurityContext();
        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));
        budgetService.deleteBudget(1L);
        verify(budgetRepository).deleteById(1L);
    }
}
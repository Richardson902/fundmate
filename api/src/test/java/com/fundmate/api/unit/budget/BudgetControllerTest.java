package com.fundmate.api.unit.budget;

import com.fundmate.api.controller.BudgetController;
import com.fundmate.api.dto.request.BudgetRequest;
import com.fundmate.api.dto.response.BudgetResponse;
import com.fundmate.api.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    private BudgetRequest budgetRequest;
    private BudgetResponse budgetResponse;

    @BeforeEach
    void setUp() {
        budgetRequest = new BudgetRequest();
        budgetRequest.setAmount(1000.0);
        budgetRequest.setCategoryId(1L);
        budgetRequest.setAccountId(1L);
        budgetRequest.setStartDate(LocalDate.now());
        budgetRequest.setEndDate(LocalDate.now().plusMonths(1));

        budgetResponse = new BudgetResponse();
        budgetResponse.setId(1L);
        budgetResponse.setAmount(1000.0);
        budgetResponse.setSpentAmount(200.0);
        budgetResponse.setResidualAmount(800.0);
        budgetResponse.setCompletionPercentage(20.0);
        budgetResponse.setStartDate(LocalDate.now());
        budgetResponse.setEndDate(LocalDate.now().plusMonths(1));
    }

    @Test
    void createBudget_ShouldReturnCreatedResponse() {
        when(budgetService.createBudget(any(BudgetRequest.class)))
                .thenReturn(budgetResponse);

        ResponseEntity<BudgetResponse> response = budgetController.createBudget(budgetRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(budgetResponse, response.getBody());
        verify(budgetService).createBudget(budgetRequest);
    }

    @Test
    void getBudgetsByAccountId_ShouldReturnList() {
        List<BudgetResponse> budgets = Arrays.asList(budgetResponse);
        when(budgetService.getBudgetsByAccountId(1L)).thenReturn(budgets);

        ResponseEntity<List<BudgetResponse>> response = budgetController.getBudgetsByAccountId(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budgets, response.getBody());
        assertEquals(1, response.getBody().size());
        verify(budgetService).getBudgetsByAccountId(1L);
    }

    @Test
    void getBudgetById_ShouldReturnBudget() {
        when(budgetService.getBudgetById(1L)).thenReturn(budgetResponse);

        ResponseEntity<BudgetResponse> response = budgetController.getBudgetById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(budgetResponse, response.getBody());
        verify(budgetService).getBudgetById(1L);
    }

    @Test
    void deleteBudgetById_ShouldReturnNoContent() {
        doNothing().when(budgetService).deleteBudget(1L);

        ResponseEntity<Void> response = budgetController.deleteBudgetById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(budgetService).deleteBudget(1L);
    }
}
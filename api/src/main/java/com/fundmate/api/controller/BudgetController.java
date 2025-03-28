package com.fundmate.api.controller;

import com.fundmate.api.dto.request.BudgetRequest;
import com.fundmate.api.dto.response.BudgetResponse;
import com.fundmate.api.service.BudgetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(@RequestBody BudgetRequest budgetRequest) {
        return new ResponseEntity<>(budgetService.createBudget(budgetRequest), HttpStatus.CREATED);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<BudgetResponse>> getBudgetsByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(budgetService.getBudgetsByAccountId(accountId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getBudgetById(@PathVariable Long id) {
        return ResponseEntity.ok(budgetService.getBudgetById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudgetById(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}

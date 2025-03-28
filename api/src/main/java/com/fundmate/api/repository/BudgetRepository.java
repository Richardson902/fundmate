package com.fundmate.api.repository;

import com.fundmate.api.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findByAccountId(Long accountId);
    boolean existsByCategoryId(Long categoryId);

}

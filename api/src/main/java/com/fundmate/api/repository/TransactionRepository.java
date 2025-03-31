package com.fundmate.api.repository;

import com.fundmate.api.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountIdOrderByDateDesc(Long accountId);
    List<Transaction> findByAccountIdAndDateBetweenOrderByDateDesc(
            Long accountId, LocalDate startDate, LocalDate endDate);
    boolean existsByCategoryId(Long categoryId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.account.id = :accountId " +
            "AND t.category.id = :categoryId AND t.date BETWEEN :startDate AND :endDate")
    Double sumAmountByAccountIdAndCategoryIdAndDateBetween(
            Long accountId, Long categoryId, LocalDate startDate, LocalDate endDate);
}

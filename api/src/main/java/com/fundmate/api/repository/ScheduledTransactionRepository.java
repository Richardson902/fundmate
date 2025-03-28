package com.fundmate.api.repository;

import com.fundmate.api.model.ScheduledTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduledTransactionRepository extends JpaRepository<ScheduledTransaction, Long> {
    List<ScheduledTransaction> findByAccountId(Long accountId);
}

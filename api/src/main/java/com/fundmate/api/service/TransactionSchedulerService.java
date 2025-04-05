package com.fundmate.api.service;

import com.fundmate.api.model.ScheduledTransaction;

import java.time.LocalDate;

public interface TransactionSchedulerService {
    void processScheduledTransactions();
    boolean isTransactionDue(ScheduledTransaction scheduledTransaction, LocalDate today);
    void createTransaction(ScheduledTransaction scheduledTransaction);
    void updateOccurrences(ScheduledTransaction scheduledTransaction);

}

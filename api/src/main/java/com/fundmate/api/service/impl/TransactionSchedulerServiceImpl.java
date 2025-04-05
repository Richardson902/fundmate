package com.fundmate.api.service.impl;

import com.fundmate.api.dto.request.TransactionRequest;
import com.fundmate.api.model.ScheduledTransaction;
import com.fundmate.api.repository.AccountRepository;
import com.fundmate.api.repository.ScheduledTransactionRepository;
import com.fundmate.api.repository.TransactionRepository;
import com.fundmate.api.service.TransactionSchedulerService;
import com.fundmate.api.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class TransactionSchedulerServiceImpl implements TransactionSchedulerService {

    private final ScheduledTransactionRepository scheduledTransactionRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public TransactionSchedulerServiceImpl(ScheduledTransactionRepository scheduledTransactionRepository, TransactionRepository transactionRepository, AccountRepository accountRepository, TransactionService transactionService) {
        this.scheduledTransactionRepository = scheduledTransactionRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void processScheduledTransactions() {
        LocalDate today = LocalDate.now();
        List<ScheduledTransaction> scheduledTransactions = scheduledTransactionRepository.findAll();

        for (ScheduledTransaction scheduledTransaction : scheduledTransactions) {
            if (isTransactionDue(scheduledTransaction, today)) {
                createTransaction(scheduledTransaction);
                updateOccurrences(scheduledTransaction);
            }
        }

    }

    @Override
    public boolean isTransactionDue(ScheduledTransaction scheduledTransaction, LocalDate today) {
        return scheduledTransaction.getOccurrences() > 0 &&
            !scheduledTransaction.getExecutionDate().isAfter(today);
    }

    @Override
    public void createTransaction(ScheduledTransaction scheduledTransaction) {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(scheduledTransaction.getAmount());
        transactionRequest.setAccountId(scheduledTransaction.getAccount().getId());
        transactionRequest.setCategoryId(scheduledTransaction.getCategory().getId());
        transactionRequest.setDate(LocalDate.now());
        transactionRequest.setFromName(scheduledTransaction.getFromName());
        transactionRequest.setNote(scheduledTransaction.getNote());
        
        transactionService.createTransaction(transactionRequest);

        // Update the execution date for the next occurrence
        LocalDate nextExecutionDate = calculateNextExecutionDate(scheduledTransaction);
        scheduledTransaction.setExecutionDate(nextExecutionDate);
        scheduledTransactionRepository.save(scheduledTransaction);
    }

    private LocalDate calculateNextExecutionDate(ScheduledTransaction scheduledTransaction) {
    LocalDate nextExecutionDate = LocalDate.now(); // Start from today
    nextExecutionDate = switch (scheduledTransaction.getRecurrenceType()) {
        case DAILY -> nextExecutionDate.plusDays(scheduledTransaction.getRecurrenceInterval());
        case WEEKLY -> nextExecutionDate.plusWeeks(scheduledTransaction.getRecurrenceInterval());
        case MONTHLY -> nextExecutionDate.plusMonths(scheduledTransaction.getRecurrenceInterval());
        case YEARLY -> nextExecutionDate.plusYears(scheduledTransaction.getRecurrenceInterval());
    };
    return nextExecutionDate;
}

    @Override
    public void updateOccurrences(ScheduledTransaction scheduledTransaction) {
        scheduledTransaction.setOccurrences(scheduledTransaction.getOccurrences() - 1);
        if (scheduledTransaction.getOccurrences() <= 0) {
            scheduledTransactionRepository.delete(scheduledTransaction);
        } else {
            scheduledTransactionRepository.save(scheduledTransaction);
        }

    }
}
